package de.Roboter007.voxiety.api.test;


import de.Roboter007.voxiety.Voxiety;

import java.util.HashMap;
import java.util.logging.Logger;

public class PerformanceTester {

    private static final HashMap<String, PerfTestResults> PERFORMANCE_TESTS = new HashMap<>();

    private final Logger perfLogger;

    public PerformanceTester(String modId) {
        this.perfLogger = Logger.getLogger(modId + "-Performance-Tester");
    }

    public PerformanceTester() {
        this(Voxiety.NAME);
    }
    
    
    public void start(String name) {
        long startNano = System.nanoTime();
        if(!PERFORMANCE_TESTS.containsKey(name)) {
            PERFORMANCE_TESTS.put(name, new PerfTestResults(startNano));
        } else {
            perfLogger.warning("Name already in Map! - " + name);
        }
    }

    public void end(String name) {
        PerfTestResults perfTest = PERFORMANCE_TESTS.get(name);
        long endNano = System.nanoTime();
        if(perfTest != null) {
            perfTest.setEndNano(endNano);
            perfLogger.info("Performance Test End for: " + name);
            perfLogger.info("Start Nano: " + perfTest.getStartNano());
            perfLogger.info("End Nano: " + endNano);
            perfLogger.info("Time Passed Nano: " + perfTest.getTimePassedNano());
            perfLogger.info("Time Passed Seconds: " + perfTest.getTimePassedSec());
        } else {
            perfLogger.info("Performance Test Not Found! - " + name);
        }
    }

    public static class PerfTestResults {

        private final long startNano;
        private long endNano;

        public PerfTestResults(long startNano) {
            this.startNano = startNano;
        }

        public long getStartNano() {
            return startNano;
        }

        public long getEndNano() {
            return endNano;
        }

        public void setEndNano(long endNano) {
            this.endNano = endNano;
        }

        public long getTimePassedNano() {
            return endNano - startNano;
        }

        public double getTimePassedSec() {
            return getTimePassedNano() / 1000000000.0D;
        }
    }

}
