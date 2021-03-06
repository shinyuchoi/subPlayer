import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class popupMenus extends JPopupMenu {
    UI ui;

    JMenuItem addMenu(String name, ActionListener actionListener) {
        JMenuItem jMenuItem = new JMenuItem(name);
        jMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    void addSubMenu(JMenu jMenu, String name, ActionListener actionListener) {
        JMenuItem jMenu1 = new JMenuItem(name);
        jMenu1.setHorizontalAlignment(SwingConstants.CENTER);
        jMenu1.addActionListener(actionListener);
        jMenu.add(jMenu1);
    }


    popupMenus(UI ui) {
        this.ui = ui;

        add(addMenu("초기화", evt -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-jar", new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath());
                pb.start();
                System.exit(0);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }));
        addSeparator();
        addSeparator();
        add(addMenu("일시정지/시간조절", new TimeControllerJOptional(ui)));

        addSeparator();
        addSeparator();
        add(addMenu("자막 선택하기", new SubSelect(ui)));


        add(addMenu("싱크표시/해제", evt -> {
            ui.subThread.indicatorInfolabel = !ui.subThread.indicatorInfolabel;
        }));

        add(addMenu("투명화/해제", evt -> {
            if (ui.isTransparent) {
                ui.initButtons();
                ui.subTextLabel.setBackground(Color.lightGray);
                ui.subTextUnderLabel.setBackground(Color.lightGray);
                ui.jFrame.setBackground(Color.lightGray);
                ui.fontSizeExplainLabel.setBackground(Color.gray);
                ui.fontSizeExplainLabel.setText("글씨 크기");
                ui.frameLocationLabel.setBackground(Color.gray);
                ui.frameLocationLabel.setText("위치 조절");
                ui.jPopupMenu.setVisible(false);
                ui.jFrame.repaint();
                ui.isTransparent = false;
            } else {
                ui.removeButtons();
                ui.doTransparentLabels();
                ui.jPopupMenu.setVisible(false);
                ui.jFrame.repaint();
                ui.isTransparent = true;
            }
        }));


        addSeparator();


        add(addSyncControlMenu());

        add(addMoveFrame());
        add(addFontsize());
        addSeparator();
        addSeparator();


        add(addMenu("사용법/도움말", e -> {
            JOptionPane.showMessageDialog(null, new HelpPanel(ui.version)
                    ,
                    "사용법/도움말",
                    JOptionPane.PLAIN_MESSAGE);

        }));
        add(addMenu("종료", e -> System.exit(0)));

    }


    JMenu addFontsize() {
        JMenu jMenu = new JMenu("폰트크기");
        jMenu.setHorizontalAlignment(SwingConstants.CENTER);

        addSubMenu(jMenu, "--", e -> {
            ui.fontSize -= 2;
            ui.subTextLabel.setFont(new Font(null, Font.BOLD, ui.fontSize));
            ui.subTextUnderLabel.setFont(new Font(null, Font.BOLD, ui.fontSize));
        });


        addSubMenu(jMenu, "크기리셋", e -> {
            ui.fontSize = 50;
            ui.subTextLabel.setFont(new Font(null, Font.BOLD, ui.fontSize));
            ui.subTextUnderLabel.setFont(new Font(null, Font.BOLD, ui.fontSize));
        });


        addSubMenu(jMenu, "++", e -> {
            ui.fontSize += 2;
            ui.subTextLabel.setFont(new Font(null, Font.BOLD, ui.fontSize));
            ui.subTextUnderLabel.setFont(new Font(null, Font.BOLD, ui.fontSize));
        });
        return jMenu;
    }


    JMenu addSyncControlMenu() {
        JMenu jMenu = new JMenu("플레이타임 조절");
        jMenu.setHorizontalAlignment(SwingConstants.CENTER);
        addSubMenu(jMenu, "<<5초", e -> {
            ui.subThread.syncConrol(-5);

        });


        addSubMenu(jMenu, "<<1초", e -> {
            ui.subThread.syncConrol(-1);
        });


        addSubMenu(jMenu, "<<0.5초", e -> {
            ui.subThread.syncConrol(-0.5);
        });


        addSubMenu(jMenu, "리셋", e -> {
            if (ui.isPlaying()) {
                ui.subThread.timeControl = 0;
                ui.subThread.arrangeIndex();
                ui.infoLabel.setText("싱크: " + (ui.subThread.timeControl / 1000) + "초");

            }
        });


        addSubMenu(jMenu, "0.5초>>", e -> {
            ui.subThread.syncConrol(0.5);

        });


        addSubMenu(jMenu, "1초>>", e -> {
            ui.subThread.syncConrol(1);

        });

        addSubMenu(jMenu, "5초>>", e -> {
            ui.subThread.syncConrol(5);

        });
        return jMenu;
    }

    JMenu addMoveFrame() {
        JMenu jMenu = new JMenu("위치 조절");
        jMenu.setHorizontalAlignment(SwingConstants.CENTER);
        addSubMenu(jMenu, "" + (char) 9650 + (char) 9650, e -> {
            ui.jFrame.setLocation(ui.jFrame.getX(), ui.jFrame.getY() - 20);
        });
        addSubMenu(jMenu, "" + (char) 9650, e -> {
            ui.jFrame.setLocation(ui.jFrame.getX(), ui.jFrame.getY() - 10);

        });
        addSubMenu(jMenu, "리셋", e -> {
            ui.jFrame.setLocation(ui.jFrame.getX(), ui.frameHeight - ui.jFrame.getHeight() - 50);

        });
        addSubMenu(jMenu, "" + (char) 9660, e -> {
            ui.jFrame.setLocation(ui.jFrame.getX(), ui.jFrame.getY() + 10);

        });
        addSubMenu(jMenu, "" + (char) 9660 + (char) 9660, e -> {
            ui.jFrame.setLocation(ui.jFrame.getX(), ui.jFrame.getY() + 20);

        });


        return jMenu;
    }

}
