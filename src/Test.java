import main.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class Test {
    private static final int ROWS = 0;
    private static final int COLS = 1;
    private static final int MAX_NUM = 2;
    private static final int TIME = 3;
    private static final int SOLVE = 4;
    private static final long TIME_OUT = 900;
    //outfile path name is: ./output/out + Datetime + .txt
    public static File outFile = new File("./output/out" + Controller.getDateTime() + ".txt");
    // public static File outFile = new File("./output/out231904.txt");
    static Controller controller;
    static String inputFolderPath1 = "./input";
    public static File inFolder = new File(inputFolderPath1);
    static List<String> res;


    public static void listFilesForFolder(final File folder) throws InterruptedException, FileNotFoundException, TimeoutException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if (fileEntry.isFile()) {
                    String fileInfo = "";
                    String fileName = "";
                    fileName = fileEntry.getName();
                    if ((fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()).equals("in")) {
                        String time = "";
                        System.out.println(fileName);
                        controller = new Controller(fileEntry);
                        /*long t1 = System.currentTimeMillis();*/
                        ExecutorService executor = Executors.newFixedThreadPool(4);
                        Future<?> future = executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    controller.run();
                                    //controller.write();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        executor.shutdown();            //        reject all further submissions

                        try {
                            future.get(TIME_OUT, TimeUnit.SECONDS);  //     wait Time (seconds) to finish
                        } catch (InterruptedException e) {    //     possible error cases
                            System.out.println("job was interrupted");
                        } catch (ExecutionException e) {
                            System.out.println("caught exception: " + e.getCause());
                        } catch (java.util.concurrent.TimeoutException e) {
                            future.cancel(true);              //     interrupt the job
                            System.out.println("timeout");
                            controller.solve = "UNKNOWN";
                            System.out.println("UNKNOWN");
                            time = "time out";
                        }
                        // wait all unfinished tasks for sec
                        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                            // force them to quit by interrupting
                            executor.shutdownNow();
                        }

                        res = controller.inFoList();
                        //System.out.println(res.get(TIME));
                        if (time != "time out") {
                            time = res.get(TIME);
                            System.out.println("\nTotal Time: " + time + " ms");
                        }

                        System.out.println("--------------------------------");
                        fileInfo += fileName + "\t" + res.get(ROWS) + "x" + res.get(COLS) + "\t" + res.get(MAX_NUM) + "\t"
                                + time + "\t" + res.get(SOLVE);
//                    System.out.println(fileInfo);
                        Controller.outputToTxt(fileInfo, outFile);
//                    System.out.println(outFile);u
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, TimeoutException {
        //new Test(300);
        listFilesForFolder(inFolder);
        //reformatInput(reformatFolder);
    }

}