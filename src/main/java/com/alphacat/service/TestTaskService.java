package com.alphacat.service;

public interface TestTaskService {

    /**
     * Test a worker's answer of the task. If correct, the worker will gain
     * credits.
     * @return whether the worker's answer is correct
     */
    boolean test(int taskId, int workerId);

    /**
     * Test whether a worker has completed all test tasks. If has, the worker's
     * state will change to 0, which means active.
     * @return whether the worker has completed all test tasks
     */
    boolean testAllFinished(int workerId);

}
