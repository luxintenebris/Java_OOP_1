import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.Timer;
import java.util.logging.Handler;


public class Calendar extends JFrame {
    private JTable table1;
    private JPanel calPanel;
    private JButton актуализацияButton;
    private JTextField a12139TextField;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton добавитьButton;
    private JTextField textField5;
    private JTextField textField6;
    private JTable table2;
    private JTable table3;

    static int dayCal = 1;
    static int dayCalend = Start.NumOfDays();
    static int step = Start.StepCalend();
    static TimeOfEv timeExp = new TimeOfEv(1, 0, 0);
    static int counter = 0;
    static Vector<Event> events = new Vector<Event>();

    public static int getDay() {
        return dayCal;
    }

    public static Vector<Event> getEv() {
        return events;
    }

    public Calendar() {
        DefaultTableModel tableModel = new DefaultTableModel();
        table1.setFillsViewportHeight(true);
        table1.setAutoCreateRowSorter(true);
        table1.setModel(tableModel);
        DefaultTableModel d = (DefaultTableModel) table1.getModel();
        for (int i = 0; i < 7; i++) {
            d.addColumn("");
        }
        int k = dayCalend;
        int d1 = 1;

        while (k > 0) {
            Vector<Integer> days = new Vector<Integer>();
            if (k > 7) {
                for (int i = 0; i <= 6; i++) {
                    days.add(d1 + i);
                }
            } else {
                for (int i = 0; i < k; i++) {
                    days.add(d1 + i);
                }
            }
            d.addRow(days);
            d1 += 7;
            k -= 7;
        }

        DefaultTableModel tableModel3 = new DefaultTableModel();
        table3.setFillsViewportHeight(true);
        table3.setAutoCreateRowSorter(true);
        table3.setModel(tableModel3);
        DefaultTableModel d3 = (DefaultTableModel) table3.getModel();
        d3.addColumn("");


        DefaultTableModel tableModel2 = new DefaultTableModel();
        table2.setFillsViewportHeight(true);
        table2.setAutoCreateRowSorter(true);
        table2.setModel(tableModel2);
        DefaultTableModel d2 = (DefaultTableModel) table2.getModel();
        d2.addColumn("");


        CreateVecPeople();
        CreateVecEv();

        boolean[] startEv = new boolean[events.size()];
        boolean[] endEv = new boolean[events.size()];
        int[] mail = new int[events.size()];
        Arrays.fill(mail, 0);

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                TimerTick();

                for (int i = 0; i < events.size(); i++) {
                    if (events.get(i).Status(timeExp).equals(EventStatus.after) && !endEv[i]) {
                        d3.insertRow(0, new Object[]{events.get(i).ShortInfo() + " ends"});
                        endEv[i] = true;
                    }
                    if (events.get(i).Status(timeExp).equals(EventStatus.now) && !startEv[i]) {
                        d3.insertRow(0, new Object[]{events.get(i).ShortInfo() + " start"});
                        startEv[i] = true;
                    }

                    if (events.get(i).GetTimeBegin().Day() - timeExp.Day() <= 3 && mail[i] < 4 - events.get(i).GetPriority()) {
                        d2.insertRow(0, new Object[]{events.get(i).MailInfo()});
                        mail[i] += 1;
                    }
                }


            }
        });
        timer.start();


        //d.addRow(new Object[]{});
        this.setContentPane(this.calPanel);
        this.pack();
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table1.rowAtPoint(evt.getPoint());
                int col = table1.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    dayCal = 1 + col + row * 7;
                    DayOfModel test = new DayOfModel();
                }
            }
        });
        добавитьButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                добавитьButton();
            }
        });
        актуализацияButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                актуализацияButton();
            }
        });


    }

    private void актуализацияButton() {
        Vector<Event> res = new Vector<Event>();
        for (int i = 0; i < events.size(); i++) {
            if (!events.get(i).GetTimeEnd().FirstLessEqSecond(timeExp)) {
                res.add(events.get(i));
            }
        }
        events = res;
    }

    private void добавитьButton() {
        String strEv = textField1.getText() + " " + textField2.getText() + " " + textField4.getText()
                + " " + textField5.getText() + " " + textField3.getText() + " " + textField6.getText();
        System.out.println(strEv);
        AddEventFromString(strEv);
    }

    public void CreateVecPeople() {
        Vector<Person> Pers = new Vector<Person>();
        Person smb;
        String strInput = "";
        File file = new File("C:\\Users\\Nami\\Downloads\\OOP\\OOP\\src\\people.txt");
        //List<String> people = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<String> people;
        try {
            people = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < people.size(); i++) {
            String n = "";
            String m = "";
            String d = "";
            String[] pers = people.get(i).split(" ");
            smb = new Person(pers[0], pers[1], pers[2]);
            Pers.add(smb);
        }

    }

    public static void AddEventFromString(String str) {
        String[] s = str.split(" ");
        Vector<Person> people = new Vector<Person>();
        for (int i = 5; i < s.length; i++) {
            String n = "";
            String m = "";
            String d = "";
            String[] pers = s[i].split(";");
            Person smb = new Person(pers[0], pers[1], pers[2]);
            people.add(smb);
        }
        TimeOfEv stEv = new TimeOfEv();
        stEv = stEv.StringToTime(s[0]);
        TimeOfEv endEv = new TimeOfEv();
        endEv = endEv.StringToTime(s[1]);
        String place = s[4];

        Event ev = new Event(stEv, endEv, Integer.parseInt(s[2].trim()), Integer.parseInt(s[3].trim()), place, people, counter, timeExp.TimeInMin(timeExp));
        IntersectConflict fl = IntersectConflict.NONE;
        for (int i = 0; i < events.size(); i++) {
            if (ev.GetTimeBegin().SecondLessEqFirst(events.get(i).GetTimeBegin())
                    && ev.GetTimeBegin().FirstLessEqSecond(events.get(i).GetTimeEnd())
                    || ev.GetTimeEnd().SecondLessEqFirst(events.get(i).GetTimeBegin())
                    && ev.GetTimeEnd().FirstLessEqSecond(events.get(i).GetTimeEnd())) {
                if (ev.GetPlace().equals(events.get(i).GetPlace())) {
                    fl = IntersectConflict.place;
                    break;
                }
                Vector<Person> persons = events.get(i).GetPeople();
                Vector<Person> personEv = ev.GetPeople();
                for (int j = 0; j < persons.size(); j++) {
                    for (int j1 = 0; j1 < personEv.size(); j1++) {
                        if (persons.get(j).Name().equals(personEv.get(j1).Name())) {
                            fl = IntersectConflict.person;
                            break;
                        }
                        if (fl == IntersectConflict.person) {
                            break;
                        }
                    }
                }
                if (fl == IntersectConflict.person) {
                    break;
                }
            }
        }
        if (fl == IntersectConflict.NONE)
            events.add(ev);
        else {
            if (fl == IntersectConflict.person) {
                JOptionPane.showMessageDialog(new JFrame(), "Error people", "Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Error place", "Dialog", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public void CreateVecEv() {
        File file = new File("C:\\Users\\Nami\\Downloads\\OOP\\OOP\\src\\input.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<String> events;
        try {
            events = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < events.size(); i++) {
            AddEventFromString(events.get(i));
        }


    }

    public void TimerTick() {
        TimeOfEv dayMax = new TimeOfEv(dayCalend, 23, 59);
        if (timeExp.FirstLessSecond(dayMax)) {
            int t = timeExp.TimeInMin(timeExp);
            t += step;
            timeExp = timeExp.MinInTime(t);
            a12139TextField.setText(timeExp.PrintTime(timeExp));
        } else {
            a12139TextField.enableInputMethods(false);
            a12139TextField.setText(dayMax.PrintTime(dayMax));
        }
    }


    public void main(String[] args) {
        Calendar test = new Calendar();
        //System.exit(0);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        calPanel = new JPanel();
        calPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 8, new Insets(0, 0, 0, 0), -1, -1));
        table1 = new JTable();
        table1.setAutoCreateColumnsFromModel(true);
        calPanel.add(table1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Начало:");
        calPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Конец:");
        calPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField2 = new JTextField();
        textField2.setText("17.12.11");
        calPanel.add(textField2, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Место:");
        calPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField3 = new JTextField();
        textField3.setText("Azkaban");
        calPanel.add(textField3, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField1 = new JTextField();
        textField1.setText("12.12.12");
        calPanel.add(textField1, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Число людей:");
        calPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Участники:");
        calPanel.add(label5, new com.intellij.uiDesigner.core.GridConstraints(5, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Приоритет:");
        calPanel.add(label6, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField4 = new JTextField();
        textField4.setText("1");
        calPanel.add(textField4, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField5 = new JTextField();
        textField5.setText("1");
        calPanel.add(textField5, new com.intellij.uiDesigner.core.GridConstraints(4, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField6 = new JTextField();
        textField6.setText("Ron;weasleyking@gmail.com;#Griffindor");
        calPanel.add(textField6, new com.intellij.uiDesigner.core.GridConstraints(5, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        добавитьButton = new JButton();
        добавитьButton.setText("Добавить");
        calPanel.add(добавитьButton, new com.intellij.uiDesigner.core.GridConstraints(6, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        a12139TextField = new JTextField();
        a12139TextField.setText("1.0.0");
        calPanel.add(a12139TextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        актуализацияButton = new JButton();
        актуализацияButton.setText("Актуализация");
        calPanel.add(актуализацияButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        table2 = new JTable();
        calPanel.add(table2, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        table3 = new JTable();
        calPanel.add(table3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return calPanel;
    }

}
