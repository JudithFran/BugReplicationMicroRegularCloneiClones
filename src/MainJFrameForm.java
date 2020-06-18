import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainJFrameForm {
    private JPanel panelMain;
    private JComboBox comboBox1;
    private JButton showSystemInfoButton;
    private JButton percentageOfCloneFragmentsButton;
    private JButton extentOfBugReplicationButton;
    private JButton percentageOfReplicatedBugsButton;
    private JButton percentageOfSevereReplicatedButton;
    private JLabel showSystemInfoLabel;
    private JLabel selectSubjectSystemLabel;
    public JLabel titleLabel;

    public MainJFrameForm() {
        showSystemInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO add your handling code here:
                String systemName = "";
                systemName = comboBox1.getSelectedItem().toString();
                //System.out.println("System selected: " + systemName);

                if(systemName == "Ctags")
                    showSystemInfoLabel.setText("<html>System's Name: Ctags <br> Programming Language: C <br> Last Revision Number: 774</html>");
                else if(systemName == "Brlcad")
                    showSystemInfoLabel.setText("<html>System's Name: Brlcad <br> Programming Language: C <br> Last Revision Number: 735</html>");
                else if(systemName == "Freecol")
                    showSystemInfoLabel.setText("<html>System's Name: Freecol <br> Programming Language: Java <br> Last Revision Number: 1950</html>");
                else if(systemName == "Carol")
                    showSystemInfoLabel.setText("<html>System's Name: Carol <br> Programming Language: Java <br> Last Revision Number: 1700</html>");
                else if(systemName == "Jabref")
                    showSystemInfoLabel.setText("<html>System's Name: Jabref <br> Programming Language: Java <br> Last Revision Number: 1545</html>");
                else
                    showSystemInfoLabel.setText("<html>Please select a valid subject system!</html>");
            }
        });
        percentageOfCloneFragmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO add your handling code here: *************************************RQ1********************************************
                try {
                    long startTime = System.currentTimeMillis();

                    String systemName = "";
                    systemName = comboBox1.getSelectedItem().toString();
                    //System.out.println("System selected in ComboBox: " + systemName);

                    InputParameters ip = new InputParameters();
                    ip.setParameters(systemName);

                    BugReplicationMicroRegularClones brm = new BugReplicationMicroRegularClones();

                    brm.bugReplication();

                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    long sTime = (long) (elapsedTime*0.001);
                    long mTime = sTime/60;
                    sTime = sTime%60;
                    long hTime;

                    if(mTime >= 60) {
                        hTime = mTime/60;
                        mTime = mTime%60;
                        System.out.println("Total execution time for RQ1 = " + hTime + " hours " + mTime + " minutes");
                    }
                    else
                        System.out.println("Total execution time for RQ1 = " + mTime + " minutes " + sTime + " seconds");

                }catch(Exception e){
                    System.out.println("Error in percentageOfCloneFragmentsButton(RQ1): " + e);
                    e.printStackTrace();
                }
            }
        });
        extentOfBugReplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO add your handling code here: ******************************************RQ2*************************************************
                try{
                    long startTime = System.currentTimeMillis();

                    String systemName = "";
                    systemName = comboBox1.getSelectedItem().toString();
                    //System.out.println("System selected in ComboBox: " + systemName);

                    InputParameters ip = new InputParameters();
                    ip.setParameters(systemName);

                    BugReplicationMicroRegularClones brm = new BugReplicationMicroRegularClones();

                    //brm.bugReplicationRQ2();

                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    long sTime = (long) (elapsedTime*0.001);
                    long mTime = sTime/60;
                    sTime = sTime%60;
                    long hTime;

                    if(mTime >= 60) {
                        hTime = mTime/60;
                        mTime = mTime%60;
                        System.out.println("Total execution time for RQ2 = " + hTime + " hours " + mTime + " minutes");
                    }
                    else
                        System.out.println("Total execution time for RQ2 = " + mTime + " minutes " + sTime + " seconds");

                }catch(Exception e){
                    System.out.println("Error in extentOfBugReplicationButton(RQ2): " + e);
                    e.printStackTrace();
                }
            }
        });

        percentageOfReplicatedBugsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO add your handling code here: *****************************************************RQ3**************************************************************
                try{
                    long startTime = System.currentTimeMillis();
                    String systemName = "";
                    systemName = comboBox1.getSelectedItem().toString();
                    //System.out.println("System selected in ComboBox: " + systemName);

                    InputParameters ip = new InputParameters();
                    ip.setParameters(systemName);

                    BugReplicationMicroRegularClones brm = new BugReplicationMicroRegularClones();

                    //brm.bugReplicationRQ3();

                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    long sTime = (long) (elapsedTime*0.001);
                    long mTime = sTime/60;
                    sTime = sTime%60;
                    long hTime;

                    if(mTime >= 60) {
                        hTime = mTime/60;
                        mTime = mTime%60;
                        System.out.println("Total execution time for RQ3 = " + hTime + " hours " + mTime + " minutes");
                    }
                    else
                        System.out.println("Total execution time for RQ3 = " + mTime + " minutes " + sTime + " seconds");
                }catch(Exception e){
                    System.out.println("Error in percentageOfReplicatedBugsButton (RQ3): " + e);
                    e.printStackTrace();
                }
            }
        });
        percentageOfSevereReplicatedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO add your handling code here: *****************************************************RQ4**************************************************************
                try{
                    long startTime = System.currentTimeMillis();
                    String systemName = "";
                    systemName = comboBox1.getSelectedItem().toString();
                    //System.out.println("System selected in ComboBox: " + systemName);

                    InputParameters ip = new InputParameters();
                    ip.setParameters(systemName);

                    BugReplicationMicroRegularClones brm = new BugReplicationMicroRegularClones();

                    //brm.bugReplicationRQ4();

                    //brm.bugReplication();

                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    long sTime = (long) (elapsedTime*0.001);
                    long mTime = sTime/60;
                    sTime = sTime%60;
                    long hTime;

                    if(mTime >= 60) {
                        hTime = mTime/60;
                        mTime = mTime%60;
                        System.out.println("Total execution time for RQ4 = " + hTime + " hours " + mTime + " minutes");
                    }
                    else
                        System.out.println("Total execution time for RQ4 = " + mTime + " minutes " + sTime + " seconds");

                }catch(Exception e){
                    System.out.println("Error in percentageOfSevereReplicatedButton (RQ4): " + e);
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainJFrameForm");
        frame.setContentPane(new MainJFrameForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
