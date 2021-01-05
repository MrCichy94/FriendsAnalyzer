import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class FriendsAnalyzer extends JFrame {

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private JPanel panelMain;
    private JButton baseFriendBtn;
    private JButton actualFriendBtn;
    private JButton compareRemovedBtn;
    private JButton compareAddBtn;
    private JButton resetBtn;

    private static String addressToFile1;
    private static String addressToFile2;

    Friends friendsBefore;
    Friends friendsAfter;

    List<Friend> yourNewFriend = new ArrayList<>();
    List<Friend> whoRemovedYou = new ArrayList<>();

    private String removedInfo;
    private String addedInfo;

    final JFileChooser fileChooser = new JFileChooser();

    public FriendsAnalyzer(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.pack();

        baseFriendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (friendsBefore == null) {
                    fileChooser.setCurrentDirectory(new File("C:\\Users\\user\\Downloads"));
                    fileChooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
                    fileChooser.showOpenDialog(baseFriendBtn);

                    try {
                        addressToFile1 = fileChooser.getSelectedFile().toString();
                        ObjectMapper objectMapper = new ObjectMapper();

                        if (addressToFile1 != null) {

                            try {
                                friendsBefore = objectMapper.readValue(new File(addressToFile1), Friends.class);
                            } catch (
                                    IOException ee) {
                                ee.printStackTrace();
                            }
                        }
                    } catch (Exception r) {
                        JOptionPane.showMessageDialog(null, "No file selected!");
                    }
                } else {JOptionPane.showMessageDialog(null, "Your file has already loaded to the program. If you want to select another one, reset the data!");}
            }
        });

        actualFriendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (friendsAfter == null) {
                    fileChooser.setCurrentDirectory(new File("C:\\Users\\user\\Desktop"));
                    fileChooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
                    fileChooser.showOpenDialog(actualFriendBtn);


                    try {
                        addressToFile2 = fileChooser.getSelectedFile().toString();
                        ObjectMapper objectMapper = new ObjectMapper();

                        if (addressToFile2 != null) {
                            try {
                                friendsAfter = objectMapper.readValue(new File(addressToFile2), Friends.class);
                            } catch (IOException ee) {
                                ee.printStackTrace();
                            }
                        }
                    } catch (Exception r) {
                        JOptionPane.showMessageDialog(null, "No file selected!");
                    }
                } else {JOptionPane.showMessageDialog(null, "Your file has already loaded to the program. If you want to select another one, reset the data!");}
            }
        });

        compareRemovedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addressToFile1 != null && addressToFile2 != null) {
                    if(whoRemovedYou.size() == 0) {
                        friendWhoRemovedYouComparer();
                    }
                    if (removedInfo == null) {
                        finishRemovedInfo();
                    }
                    JOptionPane.showMessageDialog(null, removedInfo);
                } else {JOptionPane.showMessageDialog(null, "Select files to compare!");}
            }
        });

        compareAddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addressToFile1 != null && addressToFile2 != null) {
                    if(yourNewFriend.size() == 0) {
                        friendsWhoYouAddComparer();
                    }
                    if (addedInfo == null) {
                        finishAddInfo();
                    }
                    JOptionPane.showMessageDialog(null, addedInfo);
                } else {JOptionPane.showMessageDialog(null, "Select files to compare!");}
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addressToFile1 = null;
                addressToFile2 = null;
                friendsBefore = null;
                friendsAfter = null;
                yourNewFriend.clear();
                whoRemovedYou.clear();
                removedInfo = null;
                addedInfo = null;
                JOptionPane.showMessageDialog(null, "Program data has been reset!");
            }
        });
    }

    public void friendWhoRemovedYouComparer() {
        comparerAlgorithm(friendsBefore, friendsAfter, whoRemovedYou);
    }

    public void friendsWhoYouAddComparer() {
        comparerAlgorithm(friendsAfter, friendsBefore, yourNewFriend);
    }

    public void comparerAlgorithm(Friends friends1, Friends friends2, List<Friend> newFriendsList) {
        for (int i = 0; i < friends1.getFriends().size(); i++) {
            for (int j = 0; j < friends2.getFriends().size(); j++) {
                if ((friends1.getFriends().get(i).getTimestamp().equals(friends2.getFriends().get(j).getTimestamp()))) {
                    break;
                }
                if (j == friends2.getFriends().size() - 1) newFriendsList.add(friends1.getFriends().get(i));
            }
        }
    }

    public void finishRemovedInfo() {
        polishSignConverter(whoRemovedYou);
        for(Friend n : whoRemovedYou) {
            if (removedInfo == null) {
                removedInfo = n.getName() + "\n";
            } else {
                removedInfo += n.getName() + "\n";
            }
        }
    }
    public void finishAddInfo() {
        polishSignConverter(yourNewFriend);
        for(Friend n : yourNewFriend) {
            if (addedInfo == null) {
                addedInfo = n.getName() + "\n";
            } else {
                addedInfo += n.getName() + "\n";
            }
        }
    }

    public void polishSignConverter(List<Friend> friends){
        for(Friend x : friends) {
            String nameToConvert = x.getName();
            writeOutput(nameToConvert);
            String convertedName = readInput();
            x.setName(convertedName);
        }
    }

    static void writeOutput(String str) {
        try {
            FileOutputStream fos = new FileOutputStream("test.txt");
            Writer out = new OutputStreamWriter(fos, StandardCharsets.ISO_8859_1);
            out.write(str);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readInput() {
        StringBuilder buffer = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char)ch);
            }
            in.close();
            return buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        JFrame frame = new FriendsAnalyzer("Friends Analyzer");
        frame.setVisible(true);

    }

}