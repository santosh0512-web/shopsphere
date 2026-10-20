
import java.applet.Applet;
import java.awt.Graphics;

/*
 * Historical Applet demonstration for syllabus study.
 * Java 25 does not support running Applets.
 */
@SuppressWarnings("removal")
public class HelloApplet extends Applet {

    @Override
    public void paint(Graphics g) {
        g.drawString("Welcome to ShopSphere", 40, 50);
        g.drawString("RTU Advanced Java Applet Demo", 40, 80);
    }
}
