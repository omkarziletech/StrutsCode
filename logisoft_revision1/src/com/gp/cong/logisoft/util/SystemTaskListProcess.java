package com.gp.cong.logisoft.util;

import com.logiware.bean.SystemTaskProcessBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SystemTaskListProcess {

   public static List listRunningProcesses()throws Exception {
        List processes = new ArrayList();
            String line;
            String result = "";
            BufferedReader input = null;
            // Process p = Runtime.getRuntime().exec("tasklist.exe /s cong19 /u Administrator /p  /nh");
            if (osName().contains("linux")) {
                Process p = Runtime.getRuntime().exec("ps aux");
                input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    if (!line.trim().equals("")) {
                        SystemTaskProcessBean systemTaskProcessBean = new SystemTaskProcessBean();
                        if (line.indexOf("USER") == -1) {

                            systemTaskProcessBean.setImageName(line.substring(line.indexOf(":")+3, line.length()).trim());
                            systemTaskProcessBean.setProcessId(line.substring(7, 16).trim());
                            systemTaskProcessBean.setCpuMemoryUsage(line.substring(19, 24).trim());
                            systemTaskProcessBean.setSystemTaskUser(line.substring(0, 7).trim());
                            systemTaskProcessBean.setImageStatus("Running");
                        }
                        processes.add(systemTaskProcessBean);
                    }
                }

            } else {
                Process p = Runtime.getRuntime().exec("tasklist.exe /V ");
                input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    if (!line.trim().equals("")) {
                        SystemTaskProcessBean systemTaskProcessBean = new SystemTaskProcessBean();
                        if (line.indexOf("exe") != -1) {
                            String memory = line.substring(line.indexOf("Console") + 7, line.indexOf("Running")).trim();
                            systemTaskProcessBean.setImageName(line.substring(0, line.indexOf("exe") + 3));
                            systemTaskProcessBean.setProcessId(line.substring(line.indexOf("exe") + 3, line.indexOf("Console")).trim());
                            systemTaskProcessBean.setCpuMemoryUsage(memory.substring(memory.indexOf("0") + 1, memory.indexOf("K") + 1).trim());
                            systemTaskProcessBean.setImageStatus("Running");
                        }
                        processes.add(systemTaskProcessBean);
                    }
                }
            }
            input.close();
        return processes;
    }

    private static String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

//    public static void main(String[] args) {
//        listRunningProcesses();
//    }

}
