package com.datamation.sfa.customer;

import com.datamation.sfa.settings.TaskType;

/**
 * Created by Sathiyaraja on 7/3/2018.
 */

public interface AsyncTaskListener {
    void onTaskCompleted(TaskType taskType);
}
