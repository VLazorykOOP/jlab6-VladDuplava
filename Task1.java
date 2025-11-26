import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//6 лаба

public class RotatingLine extends JPanel implements ActionListener {

    // Параметри лінії
    private final int LINE_LENGTH = 150; // Довжина відрізка
    private final int CENTER_X = 250;    // Точка, навколо якої крутимо (X)
    private final int CENTER_Y = 250;    // Точка, навколо якої крутимо (Y)

    private double angle = 0;            // Поточний кут повороту (в радіанах)
    private Color currentColor = Color.BLACK; // Поточний колір

    private Timer timer;

    public RotatingLine() {
        // Налаштування таймера: спрацьовує кожні 50 мілісекунд
        timer = new Timer(50, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Перетворюємо g у Graphics2D для кращої якості ліній
        Graphics2D g2d = (Graphics2D) g;

        // Вмикаємо згладжування (щоб лінія не була "східчастою")
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Встановлюємо товщину лінії
        g2d.setStroke(new BasicStroke(5));

        // 1. Обчислюємо координати рухомого кінця відрізка
        // Формула: x = cx + R * cos(a), y = cy + R * sin(a)
        int x2 = CENTER_X + (int) (LINE_LENGTH * Math.cos(angle));
        int y2 = CENTER_Y + (int) (LINE_LENGTH * Math.sin(angle));

        // 2. Встановлюємо колір
        g2d.setColor(currentColor);

        // 3. Малюємо лінію від центру (нерухома точка) до обчисленої точки
        g2d.drawLine(CENTER_X, CENTER_Y, x2, y2);

        // (Необов'язково) Малюємо точку центру, щоб було видно вісь обертання
        g2d.setColor(Color.RED);
        g2d.fillOval(CENTER_X - 5, CENTER_Y - 5, 10, 10);
    }

    // Цей метод викликається таймером кожні 50 мс
    @Override
    public void actionPerformed(ActionEvent e) {
        // Збільшуємо кут (швидкість обертання)
        angle += 0.1;

        // Змінюємо колір
        Random rand = new Random();
        currentColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        // Кажемо Java перемалювати вікно (викликає paintComponent)
        repaint();
    }

    public static void main(String[] args) {
        // Створення вікна
        JFrame frame = new JFrame("Лабораторна 6: Обертання відрізка");
        RotatingLine panel = new RotatingLine();

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // По центру екрану
    }
}
