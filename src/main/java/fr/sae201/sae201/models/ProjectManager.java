package fr.sae201.sae201.models;

public class ProjectManager {
    static String projectPath = "";

    public static String getProjectPath() {
        return projectPath;
    }

    public static void setProjectPath(String projectPath) {
        ProjectManager.projectPath = projectPath;
    }

    public static boolean isProjectPathDefined(){
        return !projectPath.equals("");
    }

    public static void updateTitle(){
        if (projectPath.equals("")){
            StageManager.homeStage.setTitle("Séquentiel");
        }else {
            StageManager.homeStage.setTitle("Séquentiel - " + projectPath);
        }
    }

}
