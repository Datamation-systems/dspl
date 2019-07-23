package com.datamation.sfa.helpers;


import java.util.List;

public interface UploadTaskListener {
    void onTaskCompleted(List<String> list);
}