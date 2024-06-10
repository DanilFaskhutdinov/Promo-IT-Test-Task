import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Notebook {
    final static String NOTES_PATH = "notes.txt";

    /**
     * Выводит все записи из файла NOTES_PATH
     */
    private static void commandRead() {
        BufferedReader notesReader;
        try {
            notesReader = new BufferedReader(new FileReader(NOTES_PATH));
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при открытии файла с записями - файл не найден");
            return;
        }
        try {
            while (notesReader.ready()) {
                System.out.println(notesReader.readLine());
            }
            notesReader.close();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
        }
    }

    /**
     * Добавляет новую запись с датой в файл NOTES_PATH
     */
    private static void commandWrite(String newNoteText) {
        BufferedWriter notesWriter;
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("yyyy-MM-dd");
        String newNoteDate = formatter.format(System.currentTimeMillis());
        String newNote;


        try {
            notesWriter = new BufferedWriter(new FileWriter(NOTES_PATH, true));
        } catch (IOException e) {
            System.out.println("Ошибка при открытии файла.");
            return;
        }

        newNote = String.join(" ", newNoteDate, newNoteText);
        try {
            notesWriter.append(newNote);
            notesWriter.newLine();
            notesWriter.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла.");
        }
    }

    /**
     * Выводит все записи за определённую дату из файла NOTES_PATH
     */
    private static void commandFind(String searchDate) {
        BufferedReader notesReader;

        try {
            notesReader = new BufferedReader(new FileReader(NOTES_PATH));
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при открытии файла с записями - файл не найден");
            return;
        }
        try {
            while (notesReader.ready()) {
                String line = notesReader.readLine();
                if (line.startsWith(searchDate)) {
                    System.out.println(line);
                }
            }
            notesReader.close();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
        }
    }

    /**
     * Выводит общее количестов записей, количество символов в записях и самый активный день
     */
    private static void commandStats() {
        BufferedReader notesReader;
        LinkedHashMap<String, Integer> daysStats = new LinkedHashMap<>();
        int notesQuantity = 0;
        int charsQuantity = 0;

        try {
            notesReader = new BufferedReader(new FileReader(NOTES_PATH));
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при открытии файла с записями - файл не найден");
            return;
        }
        try {
            while (notesReader.ready()) {
                String tempNote = notesReader.readLine();
                String[] splitedNote = tempNote.split(" ", 2);
                int dayStat = daysStats.getOrDefault(splitedNote[0], 0);
                daysStats.put(splitedNote[0], ++dayStat);
                notesQuantity++;
                charsQuantity += splitedNote.length > 1 ? splitedNote[1].length() : 0;
            }
            notesReader.close();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
        }
        Map.Entry<String, Integer> bestDay = daysStats.
                entrySet().
                stream().
                max(Map.Entry.comparingByValue()).
                get();
        System.out.println("Общее количество записей: " + notesQuantity);
        System.out.println("Общее количество символов в записях: " + charsQuantity);
        System.out.println("Самый активный день: " + bestDay.getKey());
        System.out.println("Количество записей в этот день: " + bestDay.getValue());
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String command;

        do {
            System.out.println("Введите команду:");
            command = in.nextLine().strip().toLowerCase();
            switch (command) {
                case ("#read"):
                    commandRead();
                    break;
                case ("#write"):
                    System.out.println("Введите новую запись:");
                    String newNoteText = in.nextLine();
                    commandWrite(newNoteText);
                    break;
                case ("#find"):
                    System.out.println("Введите дату за которую хотите увидеть записи:");
                    String searchDate = in.nextLine();
                    commandFind(searchDate);
                    break;
                case ("#statistics"):
                case ("#stats"):
                    commandStats();
                    break;
                case ("#help"):
                    System.out.println("#help - Выводит данное сообщение");
                    System.out.println("#read - Выводит все записи");
                    System.out.println("#write - Позволяет сделать новую запись");
                    System.out.println("#find - Позволяет найти запись за определённую дату");
                    System.out.println("#statistics или #stats - Выводит статистику(количестов записей, количество символов, самый активный день)");
                    System.out.println("#exit - Закрывает приложение");
                    break;
                default:
                    System.out.println("Неизвестная команда.\nВведите #help для справки.");
            }
        } while (!command.equals("#exit"));
    }
}
