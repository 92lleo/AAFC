package io.kuenzler.aafc.view;

import io.kuenzler.aafc.control.Aafc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.SwingUtilities;

import java.awt.Toolkit;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 *
 * @author lleo
 */
public class LogView extends Model {

    /**
     * Creates new form commandbuilder
     */
    final String path;
    final Aafc main;
    LogcatExecuter executer;

    public LogView(Aafc main, String path) {
    	super(500, 300);    	
        initComponents();
        this.path = path;
        this.main = main;
        executer = null;        
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_logcat = new javax.swing.JTextArea();
        ta_logcat.setLineWrap(true);
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LogView");

        ta_logcat.setEditable(false);
        ta_logcat.setColumns(20);
        ta_logcat.setRows(5);
        ta_logcat.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(ta_logcat);

        jLabel1.setText("Keep track of current logs here. Log level only affects viewing, not logging");
        
        comboBox = new JComboBox();
        
        lblNewLabel = new JLabel("Log level to show:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        				.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jLabel1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblNewLabel)
        				.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea ta_logcat;
    private JComboBox comboBox;
    private JLabel lblNewLabel;
    // End of variables declaration//GEN-END:variables

    

    /**
     *
     * @author Leonhard Künzler
     * @date 22.06.12 0:20
     */
    class LogcatExecuter extends Thread {

        private ProcessBuilder builder;
        public String command; //später private
        private Process process;
        private final Logcat logcat;
        /*
         *        
         */

        protected LogcatExecuter(final Logcat logcat) {
            this.logcat = logcat;
        }

        @Override
        public void run() {
            builder = build_command(command);
            try {
                process = builder.start();
            } catch (Exception ex) {
                updateCommandline("--Crashed while starting process (" + command + ")\n--" + ex);
                return;
            }
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            String current;
            try {
                long lastProgressBarUpdate = System.currentTimeMillis();
                while ((current = br.readLine()) != null && !isInterrupted()) {
                    line += current + "\n";
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > lastProgressBarUpdate + 250) {
                        lastProgressBarUpdate = currentTime;
                        updateCommandline(line);
                        line = "";
                    }
                }
                updateCommandline(line);
            } catch (IOException ex) {
                updateCommandline("--Crashed while reading cmd output");
            }
            //updateCommandline(line);
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                updateCommandline("--Error while waiting for " + command);
            }
        }

        /**
         *
         * @param update Text to write into commandline
         */
        private void updateCommandline(final String update) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logcat.updateCommandline(update);
                }
            });
        }

        /**
         * Forwards command to processbuilder, sets flags and executes.
         *
         * @param command command as string
         * @param mode mode to run: 0:nomode 1:adb 2:fastboot 3:cmd
         * @param wait true:join, false:ignore
         * @param show true:show update in gui, false:silent
         */
        public void runCommand(String filter) {
            this.command = "logcat" + filter;
            start();
        }

        public Process getProcess() {
            return process;
        }

        /**
         *
         * @param command auszuführender befehl
         * @return
         */
        ProcessBuilder build_command(String command) {
            String[] cmdarray, progcmdarray;
            ProcessBuilder pb = new ProcessBuilder();
            cmdarray = command.split(" ");
            progcmdarray = new String[cmdarray.length + 1];
            progcmdarray[0] = logcat.path + "adb.exe";
            for (int i = 1; i < progcmdarray.length; i++) {
                progcmdarray[i] = cmdarray[i - 1];
            }
            pb = pb.command(progcmdarray);

            return pb.redirectErrorStream(true);
        }
    }
}
