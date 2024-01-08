package pn.cg.datastorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton that handles the thread pool
 */
public class ThreadPoolMaster {

    private  ExecutorService executor;

    private static ThreadPoolMaster instance;
    private ThreadPoolMaster() {


    }

    public synchronized   static ThreadPoolMaster getInstance(){

        if (instance == null){

            instance = new ThreadPoolMaster();

            instance.executor = Executors.newFixedThreadPool(10);

        }
        return instance;

    }

    public ExecutorService getExecutor() {
        return executor;
    }


    public void TerminateThreads() {



            ThreadPoolMaster.getInstance().getExecutor().shutdownNow();

        }
    }

