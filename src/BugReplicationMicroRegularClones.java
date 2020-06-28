import java.io.*;
import java.util.ArrayList;


/**
 *
 * @author Judith
 */

class CodeFragment {

    int revision = -1;
    String filepath = "";
    int startline = -1, endline = -1;
    String changetype = "-1";

    String[] lines = new String[5000];

    public void getFragment() {

        String abs_filepath = InputParameters.pathSystem + revision + "/" + filepath;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(abs_filepath)));
            String str = "";

            int line = 0;
            int i = 0;
            while ((str = br.readLine()) != null) {
                line++;
                if (line > endline) {
                    break;
                }
                if (line >= startline && line <= endline) {
                    lines[i] = str.trim();
                    //System.out.println("lines[" + i + "] = " + lines[i]);
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("error.getFragment." + e);
            e.printStackTrace();
            System.exit(0);
        }

    }

    public void showFragment() {

        String abs_filepath = InputParameters.pathSystem + revision + "/" + filepath;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(abs_filepath)));
            String str = "";

            System.out.println("\n" + revision + ": " + filepath + ", " + startline + " - " + endline);
            //System.out.println("---------------------------------------------------");
            int line = 0;
            int i = 0;
            while ((str = br.readLine()) != null) {
                line++;
                if (line > endline) {
                    break;
                }
                if (line >= startline && line <= endline) {
                    lines[i] = str.trim();
                    i++;
                    //System.out.println(str);
                }
            }
            //System.out.println("---------------------------------------------------");
        } catch (Exception e) {
            System.out.println("error.showFragment." + e);
            e.printStackTrace();
            System.exit(0);
        }
    }
}


public class BugReplicationMicroRegularClones {

    CodeFragment[][] cfp = new CodeFragment[100000][2];

    DBConnect db = new DBConnect();
    CompareChanges cc = new CompareChanges();

    int countRevR = 0;
    int countRevRepR = 0;

    int countRevM = 0;
    int countRevRepM = 0;

    int RQ4 = 0;

    public String getBugFixCommits() {
        String bugFixCommits = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputParameters.systemName + " commitlog.txt")));

            String str = "";
            String prevString = "";

            int commit = 0;
            while ((str = br.readLine()) != null) {
                if (str.trim().length() == 0) {
                    continue;
                }

                if (prevString.contains("--------------------------------")) {
                    //this is the starting of a commit report.
                    //we need to know the commit number.
                    String str1 = str.trim().split("[ ]+")[0].trim();
                    str1 = str1.substring(1);
                    commit = Integer.parseInt(str1);
                    //System.out.println ("Testing for Deckard version. Commit = " + commit);
                } else {
                    //according to the study of Mockus
                    if (str.toLowerCase().contains("bug") || str.toLowerCase().contains("fix") || str.toLowerCase().contains("fixup") || str.toLowerCase().contains("error") || str.toLowerCase().contains("fail"))
                    //if (str.contains ("bug") || str.contains("fix") || str.contains ("fixup") || str.contains ("error") || str.contains ("fail"))
                    {
                        if (!bugFixCommits.contains(" " + commit + " ")) {
                            bugFixCommits += " " + commit + " ";
                        }
                    }
                }
                prevString = str;
            }
            br.close();
            //System.out.println ("Revisions that were created because of a bug fix = " + bugFixCommits);

        } catch (Exception e) {
            System.out.println("Error in getBugFixCommits = " + e);
            e.printStackTrace();
            System.exit(0);
        }

        return bugFixCommits;
    }

    // For RQ4 use this method

    public String getBugFixCommitsRQ4() {
        String bugFixCommits = "";
        try{
            String[] bugFixCommitsMockus = new String[10000];
            String[] bugFixCommitsLamkanfi = new String[10000];
            String[] bugFixCommitsTemp = new String[10000];

            String str1 = getBugFixCommitsMockus();
            System.out.println ("Revisions that were created because of a bug fix (Mockus) = " + str1);
            bugFixCommitsMockus = str1.trim().split("  ");

            for(int i = 0; i < bugFixCommitsMockus.length; i++)
                System.out.println ("Revisions that were created because of a bug fix in bugFixCommitsMockus["+i+"] array (Mockus) = " + bugFixCommitsMockus[i]);

            String str2 = getBugFixCommitsLamkanfi();
            System.out.println ("Revisions that were created because of a bug fix (Lamkanfi) = " + str2);
            bugFixCommitsLamkanfi = str2.trim().split("  ");

            for(int i = 0; i < bugFixCommitsLamkanfi.length; i++)
                System.out.println ("Revisions that were created because of a bug fix in bugFixCommitsLamkanfi["+i+"] array (Lamkanfi) = " + bugFixCommitsLamkanfi[i]);

            // Finding common commits in both arrays

            for (int i = 0; i < bugFixCommitsMockus.length; i++) {
                for (int j = 0; j < bugFixCommitsLamkanfi.length; j++) {
                    if (bugFixCommitsMockus[i].equals(bugFixCommitsLamkanfi[j])) {
                        // got the duplicate element
                        //if (!bugFixCommits.contains(" " + bugFixCommitsMockus[i] + " ")) {
                        bugFixCommits += " " + bugFixCommitsMockus[i] + " ";
                        //}
                    }
                }
            }
            System.out.println ("Revisions that were created because of a bug fix (Mockus and Lamkanfi) = " + bugFixCommits);

            bugFixCommitsTemp = bugFixCommits.trim().split("  ");

            for(int i = 0; i < bugFixCommitsTemp.length; i++)
                System.out.println ("Revisions that were created because of a bug fix in bugFixCommitsTemp["+i+"] array (Temp) = " + bugFixCommitsTemp[i]);


        } catch (Exception e) {
            System.out.println("Error in getBugFixCommits = " + e);
            e.printStackTrace();
            System.exit(0);
        }

        return bugFixCommits;
    }

    public String getBugFixCommitsMockus() {
        String bugFixCommits = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputParameters.systemName + " commitlog.txt")));

            String str = "";
            String prevString = "";

            int commit = 0;
            while ((str = br.readLine()) != null) {
                if (str.trim().length() == 0) {
                    continue;
                }

                if (prevString.contains("--------------------------------")) {
                    //this is the starting of a commit report.
                    //we need to know the commit number.
                    String str1 = str.trim().split("[ ]+")[0].trim();
                    str1 = str1.substring(1);
                    commit = Integer.parseInt(str1);
                    //System.out.println (commit);
                } else {
                    //according to the study of Mockus
                    if (str.toLowerCase().contains("bug") || str.toLowerCase().contains("fix") || str.toLowerCase().contains("fixup") || str.toLowerCase().contains("error") || str.toLowerCase().contains("fail"))
                    //if (str.contains ("bug") || str.contains("fix") || str.contains ("fixup") || str.contains ("error") || str.contains ("fail"))
                    {
                        if (!bugFixCommits.contains(" " + commit + " ")) {
                            bugFixCommits += " " + commit + " ";
                        }
                    }
                }
                prevString = str;
            }
            br.close();
            //System.out.println ("Revisions that were created because of a bug fix = " + bugFixCommits);

        } catch (Exception e) {
            System.out.println("Error in getBugFixCommitsMockus = " + e);
            e.printStackTrace();
            System.exit(0);
        }

        return bugFixCommits;
    }

    public String getBugFixCommitsLamkanfi() {
        String bugFixCommits = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputParameters.systemName + " commitlog.txt")));

            String str = "";
            String prevString = "";

            int commit = 0;
            while ((str = br.readLine()) != null) {
                if (str.trim().length() == 0) {
                    continue;
                }

                if (prevString.contains("--------------------------------")) {
                    //this is the starting of a commit report.
                    //we need to know the commit number.
                    String str1 = str.trim().split("[ ]+")[0].trim();
                    str1 = str1.substring(1);
                    commit = Integer.parseInt(str1);
                    //System.out.println (commit);
                } else {

                    // Severe bugs according to Lamkanfi
                    if (str.toLowerCase().contains("fault") || str.toLowerCase().contains("machin") || str.toLowerCase().contains("reboot") || str.toLowerCase().contains("reinstal") || str.toLowerCase().contains("lockup") || str.toLowerCase().contains("seemingli") || str.toLowerCase().contains("perman") || str.toLowerCase().contains("instantli") || str.toLowerCase().contains("segfault") || str.toLowerCase().contains("compil")
                            || str.toLowerCase().contains("hang") || str.toLowerCase().contains("freez") || str.toLowerCase().contains("deadlock") || str.toLowerCase().contains("thread") || str.toLowerCase().contains("slow") || str.toLowerCase().contains("anymor") || str.toLowerCase().contains("memori") || str.toLowerCase().contains("tick") || str.toLowerCase().contains("jvm") || str.toLowerCase().contains("adapt")
                            || str.toLowerCase().contains("deadlock") || str.toLowerCase().contains("sigsegv") || str.toLowerCase().contains("relat") || str.toLowerCase().contains("caus") || str.toLowerCase().contains("snapshot") || str.toLowerCase().contains("segment") || str.toLowerCase().contains("core") || str.toLowerCase().contains("unexpectedli") || str.toLowerCase().contains("build") || str.toLowerCase().contains("loop"))
                    {
                        if (!bugFixCommits.contains(" " + commit + " ")) {
                            bugFixCommits += " " + commit + " ";
                        }
                    }
                }
                prevString = str;
            }
            br.close();
            //System.out.println ("Revisions that were created because of a bug fix = " + bugFixCommits);

        } catch (Exception e) {
            System.out.println("Error in getBugFixCommitsLamkanfi = " + e);
            e.printStackTrace();
            System.exit(0);
        }

        return bugFixCommits;
    }

    public CodeFragment[][] getChangedBugFixCommits() {

        SingleChange[] changedBugFixCommits = new SingleChange[50000];
        SingleChange[][] changedBugFixCommits2D = new SingleChange[5000][5000]; // was 10000 before optimization
        CodeFragment[][] changedBugFixCommits2DNew = new CodeFragment[5000][5000];  // was 10000 before optimization

        try {
            String str = "";

            if (RQ4 == 0)
                str = getBugFixCommits();
            else if (RQ4 == 1)
                str = getBugFixCommitsRQ4();

            String[] bugFixCommits = new String[10000];

            //SingleChange[] changes = new SingleChange[10000];
            SingleChange[] changes = db.getChangedRevisions();

            /*
            for(int j=0; changes[j] != null; j++){
            System.out.println("Revision [" + j + "]= " + changes[j].revision);
            }
             */
            bugFixCommits = str.split("  ");

            /*---------------------------------------- Preprocessing bugFixCommits Start ---------------------------------------------------------------*/
            String[] bugFixCommitsReverse = new String[10000];
            int i = 0;
            for (int j = bugFixCommits.length - 1; j >= 0; j--) {
                bugFixCommitsReverse[i] = bugFixCommits[j];
                //System.out.println("Bug Fix Revision [" + i + "] in Reverse = " + bugFixCommitsReverse[i] + " Where j = " + j);
                i++;
            }
            int len = i;

            //Changing x to x-1 for revision numbers
            for (i = 0; i < len; i++) {
                bugFixCommitsReverse[i] = Integer.toString(Integer.parseInt(bugFixCommitsReverse[i].trim()) - 1);
                //System.out.println("Bug Fix Revision [" + i + "] decreased value by 1 = " + bugFixCommitsReverse[i]);
            }
            /*---------------------------------------- Preprocessing bugFixCommits End ---------------------------------------------------------------*/

            //Matching bug-fix commits with changed revisions and saving in 1D array

            int k = 0;
            for (i = 0; i < len; i++) {
                for (int j = 0; changes[j] != null; j++) {
                    if (bugFixCommitsReverse[i].equals(changes[j].revision)) {
                        changedBugFixCommits[k] = changes[j];
                        //System.out.println("Revision [" + k + "] in changedBugFixCommits = " + changedBugFixCommits[k].revision);
                        k++;
                    }
                }
            }

            //Matching bug-fix commits with changed revisions and saving in 2D array
            int a = 0, b = 0;
            for (i = 0; i < len; i++) {
                for (int j = 0; changes[j] != null; j++) {
                    if (bugFixCommitsReverse[i].equals(changes[j].revision)) {
                        changedBugFixCommits2D[a][b] = changes[j];
                        if(changes[j+1] != null){
                            if(changes[j].revision.equals(changes[j+1].revision)){
                                b++;
                                changedBugFixCommits2D[a][b] = changes[j+1];
                            }
                            else
                                a++;
                        }
                    }
                }
                b = 0;
            }
            // Changing the data type from SingleChange to CodeFragment of the 2D array
            for(i = 0; i<changedBugFixCommits2D.length; i++){
                for(int j = 0; j<changedBugFixCommits2D.length; j++){
                    if(changedBugFixCommits2D[i][j] != null){
                        changedBugFixCommits2DNew[i][j] = new CodeFragment();
                        changedBugFixCommits2DNew[i][j].revision = Integer.parseInt(changedBugFixCommits2D[i][j].revision);
                        changedBugFixCommits2DNew[i][j].startline = Integer.parseInt(changedBugFixCommits2D[i][j].startline);
                        changedBugFixCommits2DNew[i][j].endline = Integer.parseInt(changedBugFixCommits2D[i][j].endline);
                        changedBugFixCommits2DNew[i][j].filepath = changedBugFixCommits2D[i][j].filepath;
                        changedBugFixCommits2DNew[i][j].changetype = changedBugFixCommits2D[i][j].changetype;
                    }
                }
            }

            int count = 0;
            for(i = 0; i<changedBugFixCommits2D.length; i++){
                for(int j = 0; j<changedBugFixCommits2D.length; j++){
                    if(changedBugFixCommits2D[i][j] != null){
                        //System.out.println("getChangedBugFixCommits: getChangedBugFixCommits["+i+"]["+j+"].revision = " + changedBugFixCommits2D[i][j].revision
                                //+ " Filepath = " + changedBugFixCommits2D[i][j].filepath + " Startline = " + changedBugFixCommits2D[i][j].startline
                                //+ " Endline = " + changedBugFixCommits2D[i][j].endline + " Changetype = " + changedBugFixCommits2D[i][j].changetype);
                        count++;
                    }
                    else
                        break;
                }
            }
            System.out.println("Total number of changed bug-fix code fragments (CF) = " + count);

        } catch (Exception e) {
            System.out.println("Error in getChangedBugFixCommits = " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return changedBugFixCommits2DNew;
    }

    public void bugReplication(){
        try{
            // --------------------------Implementing RQ1----------------------------

            int countFragmentR = 0;
            int countFragmentM = 0;
            int countRevision = 0;

            ArrayList<CodeFragment> bugRepR = new ArrayList<>();
            ArrayList<CodeFragment> bugRepM = new ArrayList<>();


            System.out.println("---------------------------------------Regular Clone Analysis Starts Here--------------------------------------------\n");
            bugRepR = bugReplicationR();
            countFragmentR = bugRepR.size();

            System.out.println("-----------------------------------------Micro Clone Analysis Starts Here--------------------------------------------\n");
            bugRepM = bugReplicationM();
            countFragmentM = bugRepM.size();

            // Remove all duplicates
            bugRepR.removeAll(bugRepM);

            // Merge two arraylists
            bugRepR.addAll(bugRepM);

            System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro: ");
            for(int i=0; i<bugRepR.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro: i = " + i);
                bugRepR.get(i).getFragment();
                bugRepR.get(i).showFragment();
            }

            // Removing the duplicate values based on revisions only
            // Removing duplicate revisions so that we can count the distinct revisions only
            for(int i = 0; i < bugRepR.size(); i++){
                for(int j = i+1; j < bugRepR.size(); j++){

                    if(bugRepR.get(i).revision == bugRepR.get(j).revision){
                        bugRepR.remove(j);
                        j--;
                    }
                }
            }

            System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro and after removing duplicate revision number: ");
            for(int i=0; i<bugRepR.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro and after removing duplicate revision number: i = " + i);
                bugRepR.get(i).getFragment();
                bugRepR.get(i).showFragment();
            }

            countRevision = bugRepR.size();

            // Answering RQ1
            // Calculating the average number of replicated bugs for both regular and micro clones

            System.out.println("Total number of distinct clone fragments that experienced bug-replication in Regular clones = " + countFragmentR);
            System.out.println("Total number of distinct clone fragments that experienced bug-replication in Micro clones = " + countFragmentM);
            System.out.println("Total number of distinct revisions that experienced bug-replication in both Regular and Micro clones = " + countRevision);

            System.out.println("Distinct percentage of replicated bugs in regular clones per revision = " + (float) countFragmentR/countRevision);
            System.out.println("Distinct percentage of replicated bugs in micro clones per revision = " + (float) countFragmentM/countRevision);

        } catch(Exception e){
            System.out.println("Error in BugReplication() = " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    //-------------------------------------- This function implementing RQ2 ---------------------------------------

    public void bugReplicationRQ2(){
        try{

            // --------------------------Implementing RQ2 for Regular Clones----------------------------
            ArrayList<CodeFragment> bugRepR = new ArrayList<>();

            bugRepR = bugReplicationR();

            int classID1R = 0, classID2R = 0, countRepR = 1, nclonesR = 0, flagR = 0;

            for(int i = 0; i < bugRepR.size(); i++){
                for(int j = i+1; j < bugRepR.size(); j++){
                    //if(bugRepR.get(i).revision == bugRepR.get(j).revision && bugRepR.get(i).filepath.equals(bugRepR.get(j).filepath)){
                    if(bugRepR.get(i).revision == bugRepR.get(j).revision){
                        System.out.println("Revision number (R) = " + bugRepR.get(i).revision);
                        classID1R = getClassID(bugRepR.get(i));
                        classID2R = getClassID(bugRepR.get(j));

                        if(classID1R == classID2R){
                            countRepR++;
                            i++;
                        }
                        flagR = 1;

                    }

                }
                if(flagR == 1){
                    nclonesR = nclonesR + getCloneNumber(bugRepR.get(i-1));
                    flagR = 0;
                }

                System.out.println("At i = " + i + " countRepR = " + countRepR);
                System.out.println("At i = " + i + " nclonesR = " + nclonesR);

                PrintWriter writer = new PrintWriter("Temp_output.txt", "UTF-8");

                writer.println("At i = " + i + " countRepR = " + countRepR);
                writer.println("At i = " + i + " nclonesR = " + nclonesR);

                writer.close();

                countRepR++;
            }

            // --------------------------Implementing RQ2 for Micro Clones----------------------------
            ArrayList<CodeFragment> bugRepM = new ArrayList<>();

            bugRepM = bugReplicationM();

            int classID1M = 0, classID2M = 0, countRepM = 1, nclonesM = 0, flagM = 0;

            for(int i = 0; i < bugRepM.size(); i++){
                for(int j = i+1; j < bugRepM.size(); j++){
                    //if(bugRepR.get(i).revision == bugRepR.get(j).revision && bugRepR.get(i).filepath.equals(bugRepR.get(j).filepath)){
                    if(bugRepM.get(i).revision == bugRepM.get(j).revision){
                        System.out.println("Revision number (M) = " + bugRepM.get(i).revision);
                        classID1M = getClassIDMicro(bugRepM.get(i));
                        classID2M = getClassIDMicro(bugRepM.get(j));

                        if(classID1M == classID2M){
                            countRepM++;
                            i++;
                        }
                        flagM = 1;

                    }

                }
                if(flagM == 1){
                    nclonesM = nclonesM + getCloneNumberMicro(bugRepM.get(i-1));
                    flagM = 0;
                }

                System.out.println("At i = " + i + " countRepM = " + countRepM);
                System.out.println("At i = " + i + " nclonesM = " + nclonesM);
                countRepM++;
            }
        }catch(Exception e){
            System.out.println("Error in bugReplicationRQ2: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public int getCloneNumber(CodeFragment cf) {
        // In this method I use cfFile (a two dimensional array) to store each clone file (iClones clone output file). In first dimension it will store the class number (classID)
        // and in second dimension it will store each clone fragments.
        CodeFragment[][] cfFile = new CodeFragment[10000][10000];
        int numClones = 0;
        try{
            File file = new File(InputParameters.pathClone + cf.revision + ".txt"); //All Type

            if(file.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file))); // All Type

                while((br.readLine()) != null) {
                    cfFile = fileRead(cf.revision);

                    int flag = 0;
                    for (int i = 0; i < cfFile.length; i++) {
                        for (int j = 0; j < cfFile.length; j++) {
                            if (cfFile[i][j] != null) {
                                if (cf.filepath.equals(cfFile[i][j].filepath) && cf.startline == cfFile[i][j].startline && cf.endline == cfFile[i][j].endline) {
                                    flag = 1;
                                }
                                numClones = j;
                                //System.out.println("The value of variable numClones inside the inner for loop = " + numClones);
                            }
                        }
                        if (flag == 1) {
                            //System.out.println("The value of variable numClones returned from getCloneNumber = " + numClones);
                            return numClones+1;
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error in getCloneNumber: " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return 0;
    }

    public int getCloneNumberMicro(CodeFragment cf) {
        // In this method I use cfFile (a two dimensional array) to store each clone file (iClones clone output file). In first dimension it will store the class number (classID)
        // and in second dimension it will store each clone fragments.
        CodeFragment[][] cfFile = new CodeFragment[10000][10000];
        int numClones = 0;
        try{
            File file = new File(InputParameters.pathClone + cf.revision + ".txt"); //All Type

            if(file.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file))); // All Type

                while((br.readLine()) != null) {
                    cfFile = fileReadMicro(cf.revision);

                    int flag = 0;
                    for (int i = 0; i < cfFile.length; i++) {
                        for (int j = 0; j < cfFile.length; j++) {
                            if (cfFile[i][j] != null) {
                                if (cf.filepath.equals(cfFile[i][j].filepath) && cf.startline == cfFile[i][j].startline && cf.endline == cfFile[i][j].endline) {
                                    flag = 1;
                                }
                                numClones = j;
                            }
                        }
                        if (flag == 1)
                            return numClones+1;
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error in getCloneNumberMicro: " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return 0;
    }

    //-------------------------------------- This function implementing RQ3 ---------------------------------------

    public void bugReplicationRQ3(){
        try{

            // --------------------------Implementing RQ3 for Regular Clones----------------------------

            bugReplicationR();

            System.out.println("Results of RQ3 Regular code clones:");
            System.out.println("Total Number of Distinct Bugs(revision) of code clones for Regular = " + countRevR);
            System.out.println("Total Distinct Number of Replicated Bug Revision in Regular code clone = " + countRevRepR);
            System.out.println("Percentage of Replicated Bugs in Regular code clones = " + (float) countRevRepR/countRevR*100 + "%\n");

            PrintWriter writer = new PrintWriter("Temp_output.txt", "UTF-8");

            writer.println("Results of RQ3 Regular code clones:");
            writer.println("Total Number of Distinct Bugs(revision) of code clones for Regular = " + countRevR);
            writer.println("Total Distinct Number of Replicated Bug Revision in Regular code clone = " + countRevRepR);
            writer.println("Percentage of Replicated Bugs in Regular code clones = " + (float) countRevRepR/countRevR*100 + "%\n");

            writer.close();


            bugReplicationM();

            System.out.println("Results of RQ3 Micro code clones:");
            System.out.println("Total Number of Distinct Bugs(revision) of code clones for Micro = " + countRevM);
            System.out.println("Total Distinct Number of Replicated Bug Revision in Micro code clone = " + countRevRepM);
            System.out.println("Percentage of Replicated Bugs in Micro code clones = " + (float) countRevRepM/countRevM*100 + "%\n");


        }catch(Exception e){
            System.out.println("Error in bugReplicationRQ3: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    //-------------------------------------- This function implementing RQ4 ---------------------------------------

    public void bugReplicationRQ4(){
        try{

            // --------------------------Implementing RQ4 for Regular Clones----------------------------

            RQ4 = 1;

        }catch(Exception e){
            System.out.println("Error in bugReplicationRQ4: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    //-------------------------------------- This function implementing RQ5 ---------------------------------------

    public void bugReplicationRQ5(){
        try{
            // -----------------------------Implementing RQ5 for Regular Clones---------------------------------
            ArrayList<CodeFragment> bugRepR = new ArrayList<>();
            int lineNumberR = 0;
            int lineNumberRepR = 0;

            CodeFragment[][] changedBugFixCommits = new CodeFragment[500][500];   // was 10000 before optimization
            changedBugFixCommits = getChangedBugFixCommits();

            // Looping through the changed bug-fix commit 2D array
            for(int i = 0; i<changedBugFixCommits.length; i++){
                for(int j = 0; j<changedBugFixCommits.length; j++){
                    if(changedBugFixCommits[i][j] != null){
                        //System.out.println("Revision number = " + changedBugFixCommits[i][j].revision);
                        lineNumberR += countLineNumber(changedBugFixCommits[i][j].revision);
                        //System.out.println("lineNumberR = " + lineNumberR);
                    }
                }
            }

            bugRepR = bugReplicationR();

            for(int i=0; i<bugRepR.size(); i++){
                lineNumberRepR += countLineNumber(bugRepR.get(i).revision);
                //System.out.println("lineNumberRepR = " + lineNumberRepR);
            }

            //System.out.println("******************************The Percentage of Line Coverage in Regular = " + (float) lineNumberRepR/lineNumberR*100 + "***********************************");

            // -----------------------------Implementing RQ5 for Micro Clones---------------------------------
            ArrayList<CodeFragment> bugRepM = new ArrayList<>();
            int lineNumberM = 0;
            int lineNumberRepM = 0;

            // Looping through the changed bug-fix commit 2D array
            for(int i = 0; i<changedBugFixCommits.length; i++){
                for(int j = 0; j<changedBugFixCommits.length; j++){
                    if(changedBugFixCommits[i][j] != null){
                        //System.out.println("Revision number = " + changedBugFixCommits[i][j].revision);
                        lineNumberM += countLineNumberMicro(changedBugFixCommits[i][j].revision);
                        //System.out.println("lineNumberM = " + lineNumberM);
                    }
                }
            }

            bugRepM = bugReplicationM();

            for(int i=0; i<bugRepM.size(); i++){
                lineNumberRepM += countLineNumberMicro(bugRepM.get(i).revision);
                System.out.println("lineNumberRepM = " + lineNumberRepM);
            }

            //System.out.println("******************************The Percentage of Line Coverage in Micro = " + (float) lineNumberRepM/lineNumberM*100 + "***********************************");

            System.out.println("Results for RQ5 is: \n");

            System.out.println("lineNumberR = " + lineNumberR);
            System.out.println("lineNumberRepR = " + lineNumberRepR);
            System.out.println("The Percentage of Line Coverage in Regular = " + (float) lineNumberRepR/lineNumberR*100);

            System.out.println("lineNumberM = " + lineNumberM);
            System.out.println("lineNumberRepM = " + lineNumberRepM);
            System.out.println("The Percentage of Line Coverage in Micro = " + (float) lineNumberRepM/lineNumberM*100);

        }catch(Exception e){
            System.out.println("Error in bugReplicationRQ5: " + e);
            e.printStackTrace();
        }
    }

    public int countLineNumber(int rev){
        int lineNumber = 0;
        try {
            CodeFragment[] cfFile = new CodeFragment[5000];
            CodeFragment[] cfFile1 = new CodeFragment[5000];

            File fileiClones = new File(InputParameters.pathClone + rev + ".txt"); //All Type

            if (fileiClones.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileiClones))); // All Type

                String str = "";
                int i = 0;

                // Reading each clone file (each output file of iClones) and excluding micro-clones
                while ((str = br.readLine()) != null) {
                    if (str.contains(".c") || str.contains(".h") || str.contains(".java")) {
                        int startLine, endLine;
                        cfFile[i] = new CodeFragment();
                        cfFile[i].revision = rev;

                        startLine = Integer.parseInt(str.split("\\s+")[3].trim());
                        endLine = Integer.parseInt(str.split("\\s+")[4].trim());
                        int cloneSize = (endLine-startLine)+1;
                        //System.out.println("startLine = " + startLine + " endLine = " + endLine + " cloneSize = " + cloneSize);

                        if(cloneSize>4) { // For regular clones
                            cfFile[i].startline = Integer.parseInt(str.split("\\s+")[3].trim());
                            cfFile[i].endline = Integer.parseInt(str.split("\\s+")[4].trim());
                            cfFile[i].filepath = str.split("\\s+")[2].trim();
                            i++;
                        }
                    }

                }
            }

            /*
            System.out.println("\ncfFile after excluding micro clones: \n");
            for (int m = 0; m < cfFile.length; m++) {
                if (cfFile[m] != null) {
                    System.out.println("cfFile[" + m + "] Revision = " + cfFile[m].revision + " File Path = " + cfFile[m].filepath
                        + " Start Line = " + cfFile[m].startline + " End Line = " + cfFile[m].endline);
                }
                else
                    break;
            }
            */

            // Discard empty clones from the cfFile array
            int m = 0;
            for (int i = 0; i < cfFile.length; i++) {
                if (cfFile[i] != null && cfFile[i].startline != -1) {
                    cfFile1[m] = cfFile[i];
                }
                else if (cfFile[i] != null && cfFile[i].startline == -1) {
                    m--;
                }
                m++;
            }

            /*
            System.out.println("\ncfFile1 after excluding empty clones: \n");
            for (m = 0; m < cfFile1.length; m++) {
                if (cfFile1[m] != null) {
                    System.out.println("cfFile1[" + m + "] Revision = " + cfFile1[m].revision + " File Path = " + cfFile1[m].filepath
                        + " Start Line = " + cfFile1[m].startline + " End Line = " + cfFile1[m].endline);
                }
                else
                    break;
            }
            */

            for (int i = 0; i < cfFile1.length; i++){
                if (cfFile1[i] != null) {
                    int cloneSize = cfFile1[i].endline-cfFile1[i].startline+1;
                    lineNumber += cloneSize;
                }
            }

        } catch(Exception e){
            System.out.println("Error in method countLineNumber()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return lineNumber;
    }

    public int countLineNumberMicro(int rev){
        int lineNumber = 0;
        try {
            CodeFragment[] cfFile = new CodeFragment[5000];
            CodeFragment[] cfFile1 = new CodeFragment[5000];

            File fileiClones = new File(InputParameters.pathClone + rev + ".txt"); //All Type

            if (fileiClones.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileiClones))); // All Type

                String str = "";
                int i = 0;

                // Reading each clone file (each output file of iClones) and excluding micro-clones
                while ((str = br.readLine()) != null) {
                    if (str.contains(".c") || str.contains(".h") || str.contains(".java")) {
                        int startLine, endLine;
                        cfFile[i] = new CodeFragment();
                        cfFile[i].revision = rev;

                        startLine = Integer.parseInt(str.split("\\s+")[3].trim());
                        endLine = Integer.parseInt(str.split("\\s+")[4].trim());
                        int cloneSize = (endLine-startLine)+1;
                        //System.out.println("startLine = " + startLine + " endLine = " + endLine + " cloneSize = " + cloneSize);

                        if(cloneSize<5) { // For micro clones
                            cfFile[i].startline = Integer.parseInt(str.split("\\s+")[3].trim());
                            cfFile[i].endline = Integer.parseInt(str.split("\\s+")[4].trim());
                            cfFile[i].filepath = str.split("\\s+")[2].trim();
                            i++;
                        }
                    }

                }
            }

            /*
            System.out.println("\ncfFile after excluding micro clones: \n");
            for (int m = 0; m < cfFile.length; m++) {
                if (cfFile[m] != null) {
                    System.out.println("cfFile[" + m + "] Revision = " + cfFile[m].revision + " File Path = " + cfFile[m].filepath
                        + " Start Line = " + cfFile[m].startline + " End Line = " + cfFile[m].endline);
                }
                else
                    break;
            }
            */

            // Discard empty clones from the cfFile array
            int m = 0;
            for (int i = 0; i < cfFile.length; i++) {
                if (cfFile[i] != null && cfFile[i].startline != -1) {
                    cfFile1[m] = cfFile[i];
                }
                else if (cfFile[i] != null && cfFile[i].startline == -1) {
                    m--;
                }
                m++;
            }

            /*
            System.out.println("\ncfFile1 after excluding empty clones: \n");
            for (m = 0; m < cfFile1.length; m++) {
                if (cfFile1[m] != null) {
                    System.out.println("cfFile1[" + m + "] Revision = " + cfFile1[m].revision + " File Path = " + cfFile1[m].filepath
                        + " Start Line = " + cfFile1[m].startline + " End Line = " + cfFile1[m].endline);
                }
                else
                    break;
            }
            */

            for (int i = 0; i < cfFile1.length; i++){
                if (cfFile1[i] != null) {
                    int cloneSize = cfFile1[i].endline-cfFile1[i].startline+1;
                    lineNumber += cloneSize;
                }
            }

        } catch(Exception e){
            System.out.println("Error in method countLineNumber()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return lineNumber;
    }

    public ArrayList<CodeFragment> bugReplicationR(){
        ArrayList<CodeFragment> bugRep = new ArrayList<>();
        try{
            //CodeFragment[][] cloneFragmentPair = new CodeFragment[10000][2]; // was 10000 before optimization
            //cloneFragmentPair = isClonePair();

            //CodeFragment[][] cfp = new CodeFragment[10000][2]; // was 10000 before optimization
            //cfp = isClonePair();

            isClonePair();

            if(cfp != null)
                for (int i = 0; i < cfp.length; i++)
                    for (int j = 0; j < 2; j++)
                        if(cfp[i][j] != null)
                            System.out.println("bugReplicationR: cfp["+i+"]["+j+"].revision = " + cfp[i][j].revision + " Filepath = " + cfp[i][j].filepath
                                    + " Startline = " + cfp[i][j].startline + " Endline = " + cfp[i][j].endline);
                        else
                            break;

            // For RQ3
            int countRevision = 1;
            if(cfp != null)
                for (int i = 0; cfp[i][0] != null; i++)
                    if(cfp[i+1][0] != null)
                        if(cfp[i][0].revision != cfp[i+1][0].revision)
                            countRevision++;


            System.out.println("Total number of distinct bugs(revision) of code clones for Regular = " + countRevision);

            countRevR = countRevision;


            // Finding Replicated Bugs
            int numReplicatedBugFixCommits = 0;
            for(int x = 0; cfp[x][0] != null; x++){
                if(cfp[x][0] != null && cfp[x][1] != null){
                    CodeFragment[] cloneFragmentPairINR = new CodeFragment[2];

                    cloneFragmentPairINR[0] = getInstanceInNextRevision(cfp[x][0]);
                    cloneFragmentPairINR[1] = getInstanceInNextRevision(cfp[x][1]);

                    if(cloneFragmentPairINR[0] != null && cloneFragmentPairINR[1] != null){
                        if(isClonePairBinary(cloneFragmentPairINR[0], cloneFragmentPairINR[1]) == 1){
                            numReplicatedBugFixCommits++;
                            System.out.println("///////////////////////////////////////////////////////////////////////Replicated Bug Fixing Change Found (Regular)///////////////////////////////////////////////////////////////////////");
                            //System.out.println("numReplicatedBugFixCommits for Regular Clones = " + numReplicatedBugFixCommits);

                            bugRep.add(cfp[x][0]);
                            bugRep.add(cfp[x][1]);
                        }
                    }
                }
            }
            System.out.println("Total Number of Pairs of Replicated Bug-Fix Commits for Regular Clones = " + numReplicatedBugFixCommits);

            System.out.println("\nThis is the array of replicated bugs: ");
            for(int i=0; i<bugRep.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs: i = " + i);
                bugRep.get(i).getFragment();
                bugRep.get(i).showFragment();
            }

            // Removing the duplicate values
            for(int i = 0; i < bugRep.size(); i++){
                for(int j = i+1; j < bugRep.size(); j++){

                    if(bugRep.get(i).revision == bugRep.get(j).revision && bugRep.get(i).filepath.equals(bugRep.get(j).filepath)
                            && bugRep.get(i).startline == bugRep.get(j).startline && bugRep.get(i).endline == bugRep.get(j).endline){
                        bugRep.remove(j);
                        j--;
                    }
                }
            }

            System.out.println("\nThis is the array of replicated bugs after removing duplicate objects: ");
            for(int i=0; i<bugRep.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs after removing duplicate objects: i = " + i);
                bugRep.get(i).getFragment();
                bugRep.get(i).showFragment();
            }

            System.out.println("\nTotal distinct number of replicated bugs in regular code clone = " + bugRep.size()); // OUTPUT RESULT

            // Counting the distinct number replicated bug in regular clones per revision
            int countRevisionRep = 1;
            for(int i = 0; i < bugRep.size()-1; i++)
                if(bugRep.get(i).revision != bugRep.get(i+1).revision)
                    countRevisionRep++;

            // For RQ3
            System.out.println("\nTotal distinct number of replicated bug revision in regular code clone = " + countRevisionRep); // OUTPUT RESULT

            countRevRepR = countRevisionRep;

            float averageCountRegular = (float)bugRep.size()/countRevisionRep;

            System.out.println("\nDistinct number of replicated bugs in regular clones per revision = " + averageCountRegular + "\n"); // OUTPUT RESULT

            // Saving output result of regular clones to a text file.

            PrintWriter writer = new PrintWriter("Temp_output.txt", "UTF-8");

            writer.println("\nTotal distinct number of replicated bugs in regular code clone = " + bugRep.size());
            writer.println("\nTotal distinct number of replicated bug revision in regular code clone = " + countRevisionRep);
            writer.println("\nDistinct number of replicated bugs in regular clones per revision = " + averageCountRegular + "\n");

            writer.close();

        }catch(Exception e){
            System.out.println("error in BugReplicationR = " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return bugRep;
    }

    public ArrayList<CodeFragment> bugReplicationM(){
        ArrayList<CodeFragment> bugRep = new ArrayList<>();
        try{
            CodeFragment[][] cloneFragmentPair = new CodeFragment[100000][2];

            cloneFragmentPair = isClonePairMicro();

            if(cloneFragmentPair != null)
                for (int i = 0; i < cloneFragmentPair.length; i++)
                    for (int j = 0; j < 2; j++)
                        if(cloneFragmentPair[i][j] != null)
                            System.out.println("bugReplicationM: After excluding cloneFragmentPair["+i+"]["+j+"].revision = " + cloneFragmentPair[i][j].revision + " Filepath = "
                                    + cloneFragmentPair[i][j].filepath + " Startline = " + cloneFragmentPair[i][j].startline + " Endline = " + cloneFragmentPair[i][j].endline);
                        else
                            break;


            // For RQ3
            int countRevision = 1;
            if(cloneFragmentPair != null)
                for (int i = 0; cloneFragmentPair[i][0] != null; i++)
                    if(cloneFragmentPair[i+1][0] != null)
                        if(cloneFragmentPair[i][0].revision != cloneFragmentPair[i+1][0].revision)
                            countRevision++;

            System.out.println("Total number of distinct bugs(revision) of code clones for Micro = " + countRevision);

            countRevM = countRevision;

            // Finding Replicated Bugs
            int numReplicatedBugFixCommits = 0;
            for(int x = 0; cloneFragmentPair[x][0] != null; x++){ // Showing null pointer exception here. To solve it have to return null in catch block.
                if(cloneFragmentPair[x][0] != null && cloneFragmentPair[x][1] != null){
                    CodeFragment[] cloneFragmentPairINR = new CodeFragment[2];

                    cloneFragmentPairINR[0] = getInstanceInNextRevision(cloneFragmentPair[x][0]);
                    cloneFragmentPairINR[1] = getInstanceInNextRevision(cloneFragmentPair[x][1]);

                    if(cloneFragmentPairINR[0] != null && cloneFragmentPairINR[1] != null){
                        if(isClonePairBinaryMicro(cloneFragmentPairINR[0], cloneFragmentPairINR[1]) == 1){
                            numReplicatedBugFixCommits++;
                            System.out.println("////////////////////////////////////////////////////////////////////////Replicated Bug Fixing Change Found (Micro)////////////////////////////////////////////////////////////////////////");
                            System.out.println("numReplicatedBugFixCommits for Micro Clones = " + numReplicatedBugFixCommits);

                            bugRep.add(cloneFragmentPair[x][0]);
                            bugRep.add(cloneFragmentPair[x][1]);
                        }
                    }
                }
            }

            System.out.println("Total Number of Pairs of Replicated Bug-Fix Commits for Micro Clones = " + numReplicatedBugFixCommits);

            System.out.println("\nThis is the array of replicated bugs: ");
            for(int i=0; i<bugRep.size(); i++){
                System.out.println("\nThis is the array of replicated bugs: i = " + i);
                bugRep.get(i).getFragment();
                bugRep.get(i).showFragment();
            }

            // Removing the duplicate values
            for(int i = 0; i < bugRep.size(); i++){
                for(int j = i+1; j < bugRep.size(); j++){

                    if(bugRep.get(i).revision == bugRep.get(j).revision && bugRep.get(i).filepath.equals(bugRep.get(j).filepath)
                            && bugRep.get(i).startline == bugRep.get(j).startline && bugRep.get(i).endline == bugRep.get(j).endline){
                        bugRep.remove(j);
                        j--;
                    }
                }
            }

            System.out.println("\nThis is the array of replicated bugs after removing duplicate objects: ");
            for(int i=0; i<bugRep.size(); i++){
                System.out.println("\nThis is the array of replicated bugs after removing duplicate objects: i = " + i);
                bugRep.get(i).getFragment();
                bugRep.get(i).showFragment();
            }

            System.out.println("\nTotal distinct number of replicated bugs in micro code clone = " + bugRep.size());

            // Counting the distinct number replicated bug in micro clones per revision
            int countRevisionRep = 1;
            for(int i = 0; i < bugRep.size()-1; i++)
                if(bugRep.get(i).revision != bugRep.get(i+1).revision)
                    countRevisionRep++;

            // for RQ3
            System.out.println("\nTotal distinct number of replicated bug revision in micro code clone = " + countRevisionRep);

            countRevRepM = countRevisionRep;

            float averageCountMicro = (float)bugRep.size()/countRevisionRep;

            System.out.println("\nDistinct number of replicated bugs in micro clones per revision = " + averageCountMicro + "\n");

        }catch(Exception e){
            System.out.println("Error in BugReplicationM = " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return bugRep;
    }

    //public CodeFragment[][] isClonePair(){
    public void isClonePair(){
        //CodeFragment[][] cfp = new CodeFragment[5000][2];   // was 10000 before optimization
        try{
            CodeFragment[][] changedBugFixCommits = new CodeFragment[500][500];   // was 10000 before optimization
            changedBugFixCommits = getChangedBugFixCommits();

            CodeFragment[][] cfFile = new CodeFragment[1000][1000]; // It was 10000 in Deckard, 500 in NiCad
            CodeFragment[] cfFileMatch = new CodeFragment[10000]; // It was 100000 in Deckard, 5000 in NiCad
            int x = 0;

            // Looping through the changed bug-fix commit 2D array
            for(int i = 0; i<changedBugFixCommits.length; i++){
                for(int j = 0; j<changedBugFixCommits.length; j++){
                    if(changedBugFixCommits[i][j] != null){
                        //System.out.println("Revision number = " + changedBugFixCommits[i][j].revision);
                        cfFile = fileRead(changedBugFixCommits[i][j].revision);

                        /*
                        System.out.println("\ncfFile inside isClonePair() method: \n");
                        for (int m = 0; m < cfFile.length; m++) {
                            for (int n = 0; n < cfFile.length; n++) {
                                if (cfFile[m][n] != null) {
                                    System.out.println("cfFile[" + m + "][" + n + "] Revision = " + cfFile[m][n].revision + " File Path = " + cfFile[m][n].filepath
                                            + " Start Line = " + cfFile[m][n].startline + " End Line = " + cfFile[m][n].endline);
                                }
                                else
                                    break;
                            }
                        }
                        */

                        // Looping through the output file of each revision
                        for (int m = 0; m < cfFile.length; m++) {
                            for (int n = 0; n < cfFile.length; n++) {

                                if (cfFile[m][n] != null){

                                    if(changedBugFixCommits[i][j].filepath.equals(cfFile[m][n].filepath)){

                                        // Checking for matches with changed bug-fix commits with each line of clone file (Deckard output file) of a particular revision
                                        // Matches if line numbers of code fragment from changedBugFixCommits are overlapping with line numbers range of cfFile
                                        if(((changedBugFixCommits[i][j].startline >= cfFile[m][n].startline) && (changedBugFixCommits[i][j].endline <= cfFile[m][n].endline))
                                                ||((changedBugFixCommits[i][j].startline <= cfFile[m][n].startline) && (changedBugFixCommits[i][j].endline <= cfFile[m][n].endline)
                                                && (changedBugFixCommits[i][j].endline >= cfFile[m][n].startline))
                                                ||((changedBugFixCommits[i][j].startline >= cfFile[m][n].startline) && (changedBugFixCommits[i][j].endline >= cfFile[m][n].endline)
                                                && (changedBugFixCommits[i][j].startline <= cfFile[m][n].endline))){

                                            System.out.print("*********************************************** File Name matched (Regular) ***********************************************");
                                            System.out.println(" Revision number = " + changedBugFixCommits[i][j].revision);

                                            //System.out.println("Matched CF from changedBugFixCommits["+i+"]["+j+"] = " + changedBugFixCommits[i][j].filepath + " Start Line = "
                                            //+ changedBugFixCommits[i][j].startline + " End Line = " + changedBugFixCommits[i][j].endline);

                                            //System.out.println("Matched CF1 from cfFile["+m+"]["+n+"] = " + cfFile[m][n].filepath + " Start Line = " + cfFile[m][n].startline
                                            //+ " End Line = " + cfFile[m][n].endline);

                                            // Saving the matched entries into a separate 1D array
                                            cfFileMatch[x] = cfFile[m][n];
                                            x++;
                                        }
                                    }
                                }
                                else
                                    break;
                            }
                            if (cfFile[m][0] == null)
                                break;
                        }


                    }
                    else
                        break;
                }
                if(changedBugFixCommits[i][0] == null)
                    break;
            }
            int len = 0;
            for(x = 0; cfFileMatch[x] != null; x++){
                System.out.println("cfFileMatch["+x+"] Revision = " + cfFileMatch[x].revision + " Filepath = " + cfFileMatch[x].filepath
                        + " Startline = " + cfFileMatch[x].startline + " Endline = " + cfFileMatch[x].endline);
                len++;
            }
            System.out.println("len = " + len);

            // Delete duplicate values from cfFileMatch[x] array
            for(int i = 0; i < len; i++){
                for(int j = i+1; j < len; ){
                    if(cfFileMatch[i].revision == cfFileMatch[j].revision && cfFileMatch[i].filepath.equals(cfFileMatch[j].filepath)
                            && cfFileMatch[i].startline == cfFileMatch[j].startline && cfFileMatch[i].endline == cfFileMatch[j].endline){
                        for(x = j; x < len; x++){
                            cfFileMatch[x] = cfFileMatch[x+1];
                        }
                        len--;
                    }
                    else{
                        j++;
                    }
                }
            }

            System.out.println("After removing duplicate values: ");
            for(x = 0; cfFileMatch[x] != null; x++){
                System.out.println("cfFileMatch["+x+"] Revision = " + cfFileMatch[x].revision + " Filepath = " + cfFileMatch[x].filepath
                        + " Startline = " + cfFileMatch[x].startline + " Endline = " + cfFileMatch[x].endline);
            }

            int classID1 = 0, classID2 = 0;
            x = 0;
            for(int i = 0; cfFileMatch[i] != null; i++){
                for(int j = i+1; cfFileMatch[j] != null; j++){
                    if(cfFileMatch[i].revision == cfFileMatch[j].revision){
                        //System.out.println("Revision = " + cfFileMatch[i].revision);

                        classID1 = getClassID(cfFileMatch[i]);
                        //System.out.println("classID1 in Regular = " + classID1);

                        classID2 = getClassID(cfFileMatch[j]);
                        //System.out.println("classID2 in Regular = " + classID2 + "\n");

                        if(classID1 == classID2){
                            System.out.print("********************************************Pair Found (Regular)********************************************");
                            System.out.println(" Revision number = " + cfFileMatch[i].revision);

                            cfp[x][0] = cfFileMatch[i];
                            cfp[x][1] = cfFileMatch[j];
                            x++;
                        }
                    }
                }
            }

        }catch (Exception e) {
            System.out.println("Error in method isClonePair = " + e);
            e.printStackTrace();
            System.exit(0);
        }
        //return cfp;
    }

    public CodeFragment[][] isClonePairMicro(){
        CodeFragment[][] cfpMicro = new CodeFragment[100000][2];
        try{
            CodeFragment[][] changedBugFixCommits = new CodeFragment[500][500]; // was 10000 before optimization
            changedBugFixCommits = getChangedBugFixCommits();

            CodeFragment[][] cfXmlFileMicro = new CodeFragment[10000][10000]; // was 10000 before optimization

            CodeFragment[] cfXmlFileMatch = new CodeFragment[1000000];   // was 50000 before optimization

            //CodeFragment[][] cfpReg = new CodeFragment[10000][2];   // was 50000 before optimization
            //CodeFragment[][] cfp = new CodeFragment[10000][2];   // was 50000 before optimization

            int x = 0;

            // Looping through the changed bug-fix commit 2D array
            for(int i = 0; i<changedBugFixCommits.length; i++){
                for(int j = 0; j<changedBugFixCommits.length; j++){
                    if(changedBugFixCommits[i][j] != null){
                        //System.out.println("Revision number = " + changedBugFixCommits[i][j].revision);
                        cfXmlFileMicro = fileReadMicro(changedBugFixCommits[i][j].revision);
                        //System.out.println("");

                        // Looping through the each clone file (iClones clone output file) of each revision
                        for (int m = 0; m < cfXmlFileMicro.length; m++) {
                            for (int n = 0; n < cfXmlFileMicro.length; n++) {

                                if (cfXmlFileMicro[m][n] != null){

                                    if(changedBugFixCommits[i][j].filepath.equals(cfXmlFileMicro[m][n].filepath)){

                                        // Checking for matches with changed bug-fix commits with each line of clone file (iClones output file) of a particular revision
                                        // Matches if line numbers of code fragment from changedBugFixCommits are overlapping with line numbers range of cfXmlFileMicro
                                        if(((changedBugFixCommits[i][j].startline >= cfXmlFileMicro[m][n].startline) && (changedBugFixCommits[i][j].endline <= cfXmlFileMicro[m][n].endline))
                                                ||((changedBugFixCommits[i][j].startline <= cfXmlFileMicro[m][n].startline) && (changedBugFixCommits[i][j].endline <= cfXmlFileMicro[m][n].endline)
                                                && (changedBugFixCommits[i][j].endline >= cfXmlFileMicro[m][n].startline))
                                                ||((changedBugFixCommits[i][j].startline >= cfXmlFileMicro[m][n].startline) && (changedBugFixCommits[i][j].endline >= cfXmlFileMicro[m][n].endline)
                                                && (changedBugFixCommits[i][j].startline <= cfXmlFileMicro[m][n].endline))){

                                            System.out.print("*********************************************** File Name matched (Micro) ***********************************************");
                                            System.out.println(" Revision number = " + changedBugFixCommits[i][j].revision);

                                            System.out.println("Matched CF from changedBugFixCommits["+i+"]["+j+"] = " + changedBugFixCommits[i][j].filepath + " Start Line = "
                                                    + changedBugFixCommits[i][j].startline + " End Line = " + changedBugFixCommits[i][j].endline);

                                            System.out.println("Matched CF1 from cfXmlFileMicro["+m+"]["+n+"] = " + cfXmlFileMicro[m][n].filepath + " Start Line = "
                                                    + cfXmlFileMicro[m][n].startline + " End Line = " + cfXmlFileMicro[m][n].endline);

                                            // Saving the matched entries into a separate 1D array
                                            cfXmlFileMatch[x] = cfXmlFileMicro[m][n];
                                            x++;
                                        }
                                    }
                                }
                                else
                                    break;
                            }
                            if (cfXmlFileMicro[m][0] == null)
                                break;
                        }


                    }
                    else
                        break;
                }
                if(changedBugFixCommits[i][0] == null)
                    break;
            }
            int len = 0;
            for(x = 0; cfXmlFileMatch[x] != null; x++){
                System.out.println("cfXmlFileMatch["+x+"] Revision = " + cfXmlFileMatch[x].revision + " Filepath = " + cfXmlFileMatch[x].filepath
                        + " Startline = " + cfXmlFileMatch[x].startline + " Endline = " + cfXmlFileMatch[x].endline);
                len++;
            }
            System.out.println("len = " + len);

            // Delete duplicate values from cfXmlFileMatch[x] array
            for(int i = 0; i < len; i++){
                for(int j = i+1; j < len; ){
                    if(cfXmlFileMatch[i].revision == cfXmlFileMatch[j].revision && cfXmlFileMatch[i].filepath.equals(cfXmlFileMatch[j].filepath)
                            && cfXmlFileMatch[i].startline == cfXmlFileMatch[j].startline && cfXmlFileMatch[i].endline == cfXmlFileMatch[j].endline){
                        for(x = j; x < len; x++){
                            cfXmlFileMatch[x] = cfXmlFileMatch[x+1];
                        }
                        len--;
                    }
                    else{
                        j++;
                    }
                }
            }

            System.out.println("After removing duplicate values: ");
            for(x = 0; cfXmlFileMatch[x] != null; x++){
                System.out.println("cfXmlFileMatch["+x+"] Revision = " + cfXmlFileMatch[x].revision + " Filepath = " + cfXmlFileMatch[x].filepath
                        + " Startline = " + cfXmlFileMatch[x].startline + " Endline = " + cfXmlFileMatch[x].endline);
            }

            int classID1 = 0, classID2 = 0;
            x = 0;
            for(int i = 0; cfXmlFileMatch[i] != null; i++){
                for(int j = i+1; cfXmlFileMatch[j] != null; j++){
                    if(cfXmlFileMatch[i].revision == cfXmlFileMatch[j].revision){
                        //System.out.println("Revision = " + cfXmlFileMatch[i].revision);

                        classID1 = getClassIDMicro(cfXmlFileMatch[i]);
                        //System.out.println("classID1 in Micro = " + classID1);

                        classID2 = getClassIDMicro(cfXmlFileMatch[j]);
                        //System.out.println("classID2 in Micro = " + classID2 + "\n");

                        if(classID1 == classID2){
                            System.out.print("********************************************Pair Found (Micro)********************************************");
                            System.out.println(" Revision number = " + cfXmlFileMatch[i].revision);

                            cfpMicro[x][0] = cfXmlFileMatch[i];
                            cfpMicro[x][1] = cfXmlFileMatch[j];
                            x++;
                            //System.out.println("x = " + x);
                        }

                    }
                }
            }

            //cfpReg = isClonePair();
            //cfp = isClonePair();

            if(cfpMicro != null)
                for (int i = 0; i < cfpMicro.length; i++)
                    for (int j = 0; j < 2; j++)
                        if(cfpMicro[i][j] != null)
                            System.out.println("isClonePairMicro: Before excluding cfpMicro["+i+"]["+j+"].revision = " + cfpMicro[i][j].revision + " Filepath = "
                                    + cfpMicro[i][j].filepath + " Startline = " + cfpMicro[i][j].startline + " Endline = " + cfpMicro[i][j].endline);
                        else
                            break;

            // Eliminating micro-clone pairs which reside in regular clone pairs****************************IMPORTANT**********************************************
            for(int i = 0; cfpMicro[i][0] != null; i++){
                for(int j = 0; cfp[j][0] != null; j++){
                    if(cfpMicro[i][0].filepath.equals(cfp[j][0].filepath) && cfpMicro[i][1].filepath.equals(cfp[j][1].filepath)){ // Showing null pointer exception here.
                        if( (cfpMicro[i][0].startline >= cfp[j][0].startline && cfpMicro[i][0].endline <= cfp[j][0].endline)
                                &&(cfpMicro[i][1].startline >= cfp[j][1].startline && cfpMicro[i][1].endline <= cfp[j][1].endline) ){

                            System.out.println("isClonePairMicro: Deleted Pair cfpMicro["+i+"][0].revision = " + cfpMicro[i][0].revision + " Filepath = "
                                    + cfpMicro[i][0].filepath + " Startline = " + cfpMicro[i][0].startline + " Endline = " + cfpMicro[i][0].endline);
                            System.out.println("isClonePairMicro: Deleted Pair cfpReg["+j+"][0].revision = " + cfp[j][0].revision + " Filepath = "
                                    + cfp[j][0].filepath + " Startline = " + cfp[j][0].startline + " Endline = " + cfp[j][0].endline);
                            System.out.println("isClonePairMicro: Deleted Pair cfpMicro["+i+"][1].revision = " + cfpMicro[i][1].revision + " Filepath = "
                                    + cfpMicro[i][1].filepath + " Startline = " + cfpMicro[i][1].startline + " Endline = " + cfpMicro[i][1].endline);
                            System.out.println("isClonePairMicro: Deleted Pair cfpReg["+j+"][1].revision = " + cfp[j][1].revision + " Filepath = "
                                    + cfp[j][1].filepath + " Startline = " + cfp[j][1].startline + " Endline = " + cfp[j][1].endline);
                            for(x = i; cfpMicro[x][0] != null && cfpMicro[x+1][0] != null; x++){ // To solve the null pointer exception I added the condition for x+1.
                                cfpMicro[x][0] = cfpMicro[x+1][0];
                                cfpMicro[x][1] = cfpMicro[x+1][1];
                            }
                        }
                    }
                    else if(cfpMicro[i][0].filepath.equals(cfp[j][1].filepath) && cfpMicro[i][1].filepath.equals(cfp[j][0].filepath)){
                        if( (cfpMicro[i][0].startline >= cfp[j][1].startline && cfpMicro[i][0].endline <= cfp[j][1].endline)
                                &&(cfpMicro[i][1].startline >= cfp[j][0].startline && cfpMicro[i][1].endline <= cfp[j][0].endline) ){

                            System.out.println("isClonePairMicro: Deleted Pair cfpMicro["+i+"][0].revision = " + cfpMicro[i][0].revision + " Filepath = "
                                    + cfpMicro[i][0].filepath + " Startline = " + cfpMicro[i][0].startline + " Endline = " + cfpMicro[i][0].endline);
                            System.out.println("isClonePairMicro: Deleted Pair cfpReg["+j+"][0].revision = " + cfp[j][0].revision + " Filepath = "
                                    + cfp[j][0].filepath + " Startline = " + cfp[j][0].startline + " Endline = " + cfp[j][0].endline);
                            System.out.println("isClonePairMicro: Deleted Pair cfpMicro["+i+"][1].revision = " + cfpMicro[i][1].revision + " Filepath = "
                                    + cfpMicro[i][1].filepath + " Startline = " + cfpMicro[i][1].startline + " Endline = " + cfpMicro[i][1].endline);
                            System.out.println("isClonePairMicro: Deleted Pair cfpReg["+j+"][1].revision = " + cfp[j][1].revision + " Filepath = "
                                    + cfp[j][1].filepath + " Startline = " + cfp[j][1].startline + " Endline = " + cfp[j][1].endline);

                            for(x = i; cfpMicro[x][0] != null && cfpMicro[x+1][0] != null; x++){ // To solve the null pointer exception I added the condition for x+1.
                                cfpMicro[x][0] = cfpMicro[x+1][0];
                                cfpMicro[x][1] = cfpMicro[x+1][1];
                            }
                        }
                    }
                }
            }

        }catch (Exception e) {
            System.out.println("Error in method isClonePairMicro = " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return cfpMicro;
    }

    public CodeFragment[][] fileRead(int rev){
        CodeFragment[][] cfFile2 = new CodeFragment[5000][5000];
        try {
            CodeFragment[][] cfFile = new CodeFragment[5000][5000];
            CodeFragment[][] cfFile1 = new CodeFragment[5000][5000];

            File fileiClones = new File(InputParameters.pathClone + rev + ".txt"); //All Type

            if (fileiClones.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileiClones))); // All Type

                String str = "";
                String[] singleClass = new String[1000];
                int i = 0;
                int j = 0;
                int flagRegular = 0;

                // Reading each clone file (each output file of iClones) and excluding micro-clones
                while ((str = br.readLine()) != null) {
                    if (str.contains("CloneClass")) {
                        if (flagRegular == 1) {
                            i++;
                            flagRegular = 0;
                            j = 0;
                            continue;
                        }
                    }

                    if (str.contains(".c") || str.contains(".h") || str.contains(".java")) {
                        int startLine, endLine;
                        cfFile[i][j] = new CodeFragment();
                        cfFile[i][j].revision = rev;

                        startLine = Integer.parseInt(str.split("\\s+")[3].trim());
                        endLine = Integer.parseInt(str.split("\\s+")[4].trim());
                        int cloneSize = (endLine-startLine)+1;

                        //System.out.println("startLine = " + startLine + " endLine = " + endLine + " cloneSize = " + cloneSize);

                        if(cloneSize>4) { // For regular clones
                            flagRegular = 1;
                            cfFile[i][j].startline = Integer.parseInt(str.split("\\s+")[3].trim());
                            cfFile[i][j].endline = Integer.parseInt(str.split("\\s+")[4].trim());
                            cfFile[i][j].filepath = str.split("\\s+")[2].trim();
                            j++;
                        }
                    }

                }
            }

            /*
            System.out.println("\ncfFile after excluding micro clones: \n");
            for (int m = 0; m < cfFile.length; m++) {
                for (int n = 0; n < cfFile.length; n++) {
                    if (cfFile[m][n] != null) {
                        System.out.println("cfFile[" + m + "][" + n + "] Revision = " + cfFile[m][n].revision + " File Path = " + cfFile[m][n].filepath
                                + " Start Line = " + cfFile[m][n].startline + " End Line = " + cfFile[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */

            // Discard empty clones from the cfFile array
            int m = 0;
            int n;
            for (int i = 0; i < cfFile.length; i++) {
                n = 0;
                for (int j = 0; j < cfFile.length; j++) {
                    if (cfFile[i][j] != null && cfFile[i][j].startline != -1) {
                        cfFile1[m][n] = cfFile[i][j];
                        n++;
                    }
                    else if (cfFile[i][j] != null && cfFile[i][j].startline == -1) {
                        m--;
                    }
                }
                m++;
            }

            /*
            System.out.println("\ncfFile1 after excluding empty clones: \n");
            for (m = 0; m < cfFile1.length; m++) {
                for (n = 0; n < cfFile1.length; n++) {
                    if (cfFile1[m][n] != null) {
                        System.out.println("cfFile1[" + m + "][" + n + "] Revision = " + cfFile1[m][n].revision + " File Path = " + cfFile1[m][n].filepath
                                + " Start Line = " + cfFile1[m][n].startline + " End Line = " + cfFile1[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */

            // Discard those clone classes which contain only one clone.
            m = 0;
            for (int i = 0; i < cfFile1.length; i++) {
                n = 0;
                for (int j = 0; j < cfFile1.length; j++) {
                    if (cfFile1[i][j] != null && cfFile1[i][1] != null) {
                        cfFile2[m][n] = cfFile1[i][j];
                        n++;
                    }
                    else if (cfFile1[i][j] != null && cfFile1[i][1] == null) {
                        //System.out.println("Single clone class found.");
                        m--;
                    }
                }
                m++;
            }

            /*
            System.out.println("\ncfFile2 after excluding single clone classes: \n");
            for (m = 0; m < cfFile2.length; m++) {
                for (n = 0; n < cfFile2.length; n++) {
                    if (cfFile2[m][n] != null) {
                        System.out.println("cfFile2[" + m + "][" + n + "] Revision = " + cfFile2[m][n].revision + " File Path = " + cfFile2[m][n].filepath
                                + " Start Line = " + cfFile2[m][n].startline + " End Line = " + cfFile2[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */

        } catch(Exception e){
            System.out.println("Error in method fileRead()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return cfFile2;
    }

    public CodeFragment[][] fileReadMicro(int rev){
        CodeFragment[][] cfFile2 = new CodeFragment[5000][5000];
        try {
            CodeFragment[][] cfFile = new CodeFragment[5000][5000];
            CodeFragment[][] cfFile1 = new CodeFragment[5000][5000];

            File fileiClones = new File(InputParameters.pathClone + rev + ".txt"); //All Type

            if (fileiClones.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileiClones))); // All Type

                String str = "";
                String[] singleClass = new String[1000];
                int i = 0;
                int j = 0;
                int flagRegular = 0;

                // Reading each clone file (each output file of iClones) and excluding micro-clones
                while ((str = br.readLine()) != null) {
                    if (str.contains("CloneClass")) {
                        if (flagRegular == 1) {
                            i++;
                            flagRegular = 0;
                            j = 0;
                            continue;
                        }
                    }

                    if (str.contains(".c") || str.contains(".h") || str.contains(".java")) {
                        int startLine, endLine;
                        cfFile[i][j] = new CodeFragment();
                        cfFile[i][j].revision = rev;

                        startLine = Integer.parseInt(str.split("\\s+")[3].trim());
                        endLine = Integer.parseInt(str.split("\\s+")[4].trim());
                        int cloneSize = (endLine-startLine)+1;

                        //System.out.println("startLine = " + startLine + " endLine = " + endLine + " cloneSize = " + cloneSize);

                        if(cloneSize<5) {    // For micro clones
                            flagRegular = 1;
                            cfFile[i][j].startline = Integer.parseInt(str.split("\\s+")[3].trim());
                            cfFile[i][j].endline = Integer.parseInt(str.split("\\s+")[4].trim());
                            cfFile[i][j].filepath = str.split("\\s+")[2].trim();
                            j++;
                        }
                    }

                }
            }

            /*
            System.out.println("\ncfFile after excluding micro clones: \n");
            for (int m = 0; m < cfFile.length; m++) {
                for (int n = 0; n < cfFile.length; n++) {
                    if (cfFile[m][n] != null) {
                        System.out.println("cfFile[" + m + "][" + n + "] Revision = " + cfFile[m][n].revision + " File Path = " + cfFile[m][n].filepath
                                + " Start Line = " + cfFile[m][n].startline + " End Line = " + cfFile[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */

            // Discard empty clones from the cfFile array
            int m = 0;
            int n;
            for (int i = 0; i < cfFile.length; i++) {
                n = 0;
                for (int j = 0; j < cfFile.length; j++) {
                    if (cfFile[i][j] != null && cfFile[i][j].startline != -1) {
                        cfFile1[m][n] = cfFile[i][j];
                        n++;
                    }
                    else if (cfFile[i][j] != null && cfFile[i][j].startline == -1) {
                        m--;
                    }
                }
                m++;
            }

            /*
            System.out.println("\ncfFile1 after excluding empty clones: \n");
            for (m = 0; m < cfFile1.length; m++) {
                for (n = 0; n < cfFile1.length; n++) {
                    if (cfFile1[m][n] != null) {
                        System.out.println("cfFile1[" + m + "][" + n + "] Revision = " + cfFile1[m][n].revision + " File Path = " + cfFile1[m][n].filepath
                                + " Start Line = " + cfFile1[m][n].startline + " End Line = " + cfFile1[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */

            // Discard those clone classes which contain only one clone.
            m = 0;
            for (int i = 0; i < cfFile1.length; i++) {
                n = 0;
                for (int j = 0; j < cfFile1.length; j++) {
                    if (cfFile1[i][j] != null && cfFile1[i][1] != null) {
                        cfFile2[m][n] = cfFile1[i][j];
                        n++;
                    }
                    else if (cfFile1[i][j] != null && cfFile1[i][1] == null) {
                        //System.out.println("Single clone class found.");
                        m--;
                    }
                }
                m++;
            }

            /*
            System.out.println("\ncfFile2 after excluding single clone classes: \n");
            for (m = 0; m < cfFile2.length; m++) {
                for (n = 0; n < cfFile2.length; n++) {
                    if (cfFile2[m][n] != null) {
                        System.out.println("cfFile2[" + m + "][" + n + "] Revision = " + cfFile2[m][n].revision + " File Path = " + cfFile2[m][n].filepath
                                + " Start Line = " + cfFile2[m][n].startline + " End Line = " + cfFile2[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */

        } catch(Exception e){
            System.out.println("Error in method fileReadMicro()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return cfFile2;
    }

    public int getClassID(CodeFragment cf){

        // In this method I use cfFile (a two dimensional array) to store each clone file (iClones clone output file). In first dimension it will store the class number (classID)
        // and in second dimension it will store each clone fragments.
        CodeFragment[][] cfFile = new CodeFragment[1000][1000];
        int classID;
        try{
            ArrayList<CodeFragment> cfFileMicro = new ArrayList<>();

            File file = new File(InputParameters.pathClone + cf.revision + ".txt"); //All Type

            if(file.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file))); // All Type

                cfFile = fileRead(cf.revision);

                int flag = 0;
                for (int i = 0; i < cfFile.length; i++)
                    for (int j = 0; j < cfFile.length; j++)
                        if (cfFile[i][j] != null){
                            if (cf.filepath.equals(cfFile[i][j].filepath) && cf.startline == cfFile[i][j].startline && cf.endline == cfFile[i][j].endline) {
                                flag = 1;
                                classID = i;
                                return classID;
                            }
                            if (flag == 1)
                                break;
                        }
                        else
                            break;
            }

        } catch(Exception e){
            System.out.println("Error in method getClassID()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return 0;

    }

    public int getClassIDMicro(CodeFragment cf){

        // In this method I use cfFile (a two dimensional array) to store each clone file (iClones clone output file). In first dimension it will store the class number (classID)
        // and in second dimension it will store each clone fragments.
        CodeFragment[][] cfFile = new CodeFragment[1000][1000];
        int classID;
        try{
            //ArrayList<CodeFragment> cfFileMicro = new ArrayList<>();

            File file = new File(InputParameters.pathClone + cf.revision + ".txt"); //All Type

            if(file.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file))); // All Type

                cfFile = fileReadMicro(cf.revision);

                int flag = 0;
                for (int i = 0; i < cfFile.length; i++)
                    for (int j = 0; j < cfFile.length; j++)
                        if (cfFile[i][j] != null){
                            if (cf.filepath.equals(cfFile[i][j].filepath) && cf.startline == cfFile[i][j].startline && cf.endline == cfFile[i][j].endline) {
                                flag = 1;
                                classID = i;
                                return classID;
                            }
                            if (flag == 1)
                                break;
                        }
                        else
                            break;
            }

        } catch(Exception e){
            System.out.println("Error in method getClassIDMicro()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return 0;
    }

    public int isClonePairBinary(CodeFragment cf1, CodeFragment cf2){
        int pair = 0;
        try{
            int classID1 = 0, classID2 = 0;

            classID1 = getClassID(cf1);

            classID2 = getClassID(cf2);

            if(classID1 == classID2){
                pair = 1;
            }
        } catch(Exception e){
            System.out.println("Error in method isClonePairBinary()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return pair;

    }

    public int isClonePairBinaryMicro(CodeFragment cf1, CodeFragment cf2){
        int pair = 0;
        try{
            int classID1 = 0, classID2 = 0;

            classID1 = getClassIDMicro(cf1);

            classID2 = getClassIDMicro(cf2);

            if(classID1 == classID2){
                pair = 1;
            }
        } catch(Exception e){
            System.out.println("Error in method isClonePairBinaryMicro()." + e);
            e.printStackTrace();
            System.exit(0);
        }
        return pair;
    }

    public CodeFragment getInstanceInNextRevision(CodeFragment cf) {
        try {
            CodeFragment instance = new CodeFragment();

            int crevision = cf.revision;
            int nrevision = crevision + 1;

            int nstartline = 999999999;
            int nendline = -1;

            int changed = 0;


            String cfilepath = InputParameters.pathSystem + crevision + "/" + cf.filepath;
            String nfilepath = InputParameters.pathSystem + nrevision + "/" + cf.filepath;

            File file = new File(nfilepath);
            if (!file.exists()) {
                return null;
            }

            String[][] filecompare = cc.compareFiles(cfilepath, nfilepath);

            for (int i = 0; filecompare[i][0] != null; i++) {
                String ln = filecompare[i][0].trim();
                if (ln.length() == 0) {
                    continue;
                }
                int line = Integer.parseInt(ln);
                if (line > cf.endline) {
                    break;
                }
                if (line == 509) {
                    int a = 10;
                }
                if (line >= cf.startline && line <= cf.endline) {
                    String nln = filecompare[i][2].trim(); // SHOWING NULL POINTER EXCEPTION HERE
                    if (nln.trim().length() > 0) {
                        int nline = Integer.parseInt(nln);
                        if (nstartline > nline) {
                            nstartline = nline;
                        }
                        if (nendline < nline) {
                            nendline = nline;
                        }
                    }
                    if (!filecompare[i][1].trim().equals(filecompare[i][3].trim())) {
                        if (filecompare[i][1].trim().length() > 0 || filecompare[i][3].trim().length() > 0) {
                            changed = 1;
                        }
                    }
                }
            }

            if (nendline == -1) {
                return null;
            }

            instance.revision = nrevision;
            instance.filepath = cf.filepath;
            instance.startline = nstartline;
            instance.endline = nendline;
            instance.changetype = cf.changetype;

            return instance;

        } catch (Exception e) {
            return null;
        }
    }

}
