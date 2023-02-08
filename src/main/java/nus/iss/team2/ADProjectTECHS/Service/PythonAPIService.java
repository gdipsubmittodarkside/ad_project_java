package nus.iss.team2.ADProjectTECHS.Service;

import java.io.IOException;
import java.util.List;


public interface PythonAPIService {
    void getAllAPICourseData(List<String> keywords) throws IOException, InterruptedException;
    void getAPICourseData(String keyword) throws IOException, InterruptedException;

    void getAllAPIJobData(List<String> keywords) throws IOException, InterruptedException;
    void getAPIJobData(String keyword) throws IOException, InterruptedException;
}
