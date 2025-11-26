import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// 1. ВЛАСНЕ ВИКЛЮЧЕННЯ (наслідується від ArithmeticException, як в умові)
class NegativeDivisorException extends ArithmeticException {
    public NegativeDivisorException(String message) {
        super(message);
    }
}

public class SwingApp extends JFrame {

    // Компоненти GUI
    private JTextField filePathField;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public SwingApp() {
        // Налаштування головного вікна (JFrame)
        setTitle("Лабораторна робота: Swing + Виключення");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- ВЕРХНЯ ПАНЕЛЬ (JPanel, JLabel, JTextField, JButton) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel fileLabel = new JLabel("Шлях до файлу:");
        filePathField = new JTextField("input.txt", 20); // Значення за замовчуванням
        JButton loadButton = new JButton("Завантажити та обчислити");

        topPanel.add(fileLabel);
        topPanel.add(filePathField);
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        // --- ЦЕНТРАЛЬНА ПАНЕЛЬ (JTable) ---
        // Створюємо заголовки стовпців
        String[] columnNames = {"Число A", "Число B", "Результат / Помилка"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);

        // Додаємо таблицю в панель прокрутки (щоб були заголовки і скрол)
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- ОБРОБКА ПОДІЇ НАТИСКАННЯ КНОПКИ ---
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile();
            }
        });
    }

    // Основна логіка читання та обчислення
    private void processFile() {
        // Очищаємо таблицю перед новим завантаженням
        tableModel.setRowCount(0);

        String fileName = filePathField.getText();
        File file = new File(fileName);

        // БЛОК ОБРОБКИ ВИКЛЮЧЕНЬ
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException ex) {
            // СТАНДАРТНЕ ВИКЛЮЧЕННЯ 1: Помилка відкриття файлу
            JOptionPane.showMessageDialog(this,
                    "Файл не знайдено або помилка читання: " + ex.getMessage(),
                    "Помилка файлу", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processLine(String line) {
        String inputA = "";
        String inputB = "";

        try {
            // Розбиваємо рядок на частини
            String[] parts = line.split("\\s+"); // розділення пробілами

            if (parts.length < 2) {
                tableModel.addRow(new Object[]{line, "", "Невірний формат даних"});
                return;
            }

            inputA = parts[0];
            inputB = parts[1];

            // Парсинг чисел
            double a = Double.parseDouble(inputA);
            double b = Double.parseDouble(inputB);

            // СТАНДАРТНЕ ВИКЛЮЧЕННЯ 2 (логічне): Ділення на нуль
            // (Double.parseDouble не кидає ArithmeticException при діленні на 0.0, 
            // воно дає Infinity, тому перевіримо вручну для демонстрації)
            if (b == 0) {
                throw new ArithmeticException("Ділення на нуль!");
            }

            // ВЛАСНЕ ВИКЛЮЧЕННЯ: Придумана умова (наприклад, дільник не може бути від'ємним)
            if (b < 0) {
                throw new NegativeDivisorException("Власне виключення: B < 0");
            }

            // --- ТУТ ТВОЯ ЛОГІКА З ЛАБИ 1 (Зараз: просто ділення) ---
            double result = a / b;

            // Додаємо успішний результат у таблицю
            tableModel.addRow(new Object[]{a, b, result});

        } catch (NumberFormatException ex) {
            // СТАНДАРТНЕ ВИКЛЮЧЕННЯ 2 (формат): Невірний формат числа (напр. букви)
            tableModel.addRow(new Object[]{inputA, inputB, "Помилка: Це не число"});
        } catch (NegativeDivisorException ex) {
            // ОБРОБКА ВЛАСНОГО ВИКЛЮЧЕННЯ
            tableModel.addRow(new Object[]{inputA, inputB, ex.getMessage()});
        } catch (ArithmeticException ex) {
            // ОБРОБКА СТАНДАРТНОГО АРИФМЕТИЧНОГО ВИКЛЮЧЕННЯ
            tableModel.addRow(new Object[]{inputA, inputB, ex.getMessage()});
        }
    }

    public static void main(String[] args) {
        // Запуск програми у потоці подій Swing
        SwingUtilities.invokeLater(() -> {
            new SwingApp().setVisible(true);
        });
    }
}
