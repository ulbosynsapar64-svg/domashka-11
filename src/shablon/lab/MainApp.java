package shablon.lab;

import java.util.*;

// ===== ФАСАД (FACADE) =====
class TV {
    void on() { System.out.println("Телевизор қосылды"); }
    void off() { System.out.println("Телевизор өшірілді"); }
    void setChannel(int channel) { System.out.println("Канал ауыстырылды: " + channel); }
}

class AudioSystem {
    void on() { System.out.println("Аудиожүйе қосылды"); }
    void off() { System.out.println("Аудиожүйе өшірілді"); }
    void setVolume(int level) { System.out.println("Дыбыс деңгейі: " + level); }
}

class DVDPlayer {
    void on() { System.out.println("DVD-проигрыватель қосылды"); }
    void play() { System.out.println("Фильм ойнатылуда"); }
    void pause() { System.out.println("Фильм тоқтатылды"); }
    void stop() { System.out.println("Фильм тоқтатылды"); }
    void off() { System.out.println("DVD-проигрыватель өшірілді"); }
}

class GameConsole {
    void on() { System.out.println("Ойын консолі қосылды"); }
    void playGame(String game) { System.out.println("Ойын басталды: " + game); }
    void off() { System.out.println("Ойын консолі өшірілді"); }
}

class HomeTheaterFacade {
    private TV tv;
    private AudioSystem audio;
    private DVDPlayer dvd;
    private GameConsole console;

    HomeTheaterFacade(TV tv, AudioSystem audio, DVDPlayer dvd, GameConsole console) {
        this.tv = tv;
        this.audio = audio;
        this.dvd = dvd;
        this.console = console;
    }

    void watchMovie() {
        System.out.println("=== Фильм қарау режимі ===");
        tv.on();
        audio.on();
        dvd.on();
        audio.setVolume(7);
        tv.setChannel(3);
        dvd.play();
    }

    void stopMovie() {
        System.out.println("=== Фильм тоқтатылды ===");
        dvd.stop();
        dvd.off();
        audio.off();
        tv.off();
    }

    void playGame(String game) {
        System.out.println("=== Ойын режимі ===");
        tv.on();
        audio.on();
        console.on();
        audio.setVolume(8);
        console.playGame(game);
    }

    void listenMusic() {
        System.out.println("=== Музыка тыңдау режимі ===");
        tv.on();
        audio.on();
        audio.setVolume(5);
        tv.setChannel(7);
        System.out.println("Аудио TV арқылы ойнатылуда");
    }

    void turnOffAll() {
        System.out.println("=== Барлық жүйе өшірілді ===");
        tv.off();
        audio.off();
        dvd.off();
        console.off();
    }

    void setVolume(int level) {
        audio.setVolume(level);
    }
}

// ===== КОМПОНОВЩИК (COMPOSITE) =====
abstract class FileSystemComponent {
    String name;
    FileSystemComponent(String name) { this.name = name; }
    abstract void display(String indent);
    abstract int getSize();
}

class File extends FileSystemComponent {
    int size;
    File(String name, int size) {
        super(name);
        this.size = size;
    }
    void display(String indent) {
        System.out.println(indent + "- Файл: " + name + " (" + size + "KB)");
    }
    int getSize() {
        return size;
    }
}

class Directory extends FileSystemComponent {
    List<FileSystemComponent> components = new ArrayList<>();
    Directory(String name) { super(name); }
    void addComponent(FileSystemComponent component) {
        if (!components.contains(component)) components.add(component);
    }
    void removeComponent(FileSystemComponent component) {
        components.remove(component);
    }
    void display(String indent) {
        System.out.println(indent + "[Папка] " + name);
        for (FileSystemComponent c : components) c.display(indent + "   ");
    }
    int getSize() {
        int total = 0;
        for (FileSystemComponent c : components) total += c.getSize();
        return total;
    }
}

// ===== CLIENT =====
public class MainApp {
    public static void main(String[] args) {
        System.out.println("=== ФАСАД ПАТТЕРН ===");
        TV tv = new TV();
        AudioSystem audio = new AudioSystem();
        DVDPlayer dvd = new DVDPlayer();
        GameConsole console = new GameConsole();
        HomeTheaterFacade home = new HomeTheaterFacade(tv, audio, dvd, console);
        home.watchMovie();
        home.setVolume(9);
        home.stopMovie();
        home.playGame("FIFA 25");
        home.listenMusic();
        home.turnOffAll();

        System.out.println("\n=== КОМПОНОВЩИК ПАТТЕРН ===");
        Directory root = new Directory("Root");
        Directory docs = new Directory("Documents");
        Directory music = new Directory("Music");
        File f1 = new File("resume.docx", 120);
        File f2 = new File("report.pdf", 300);
        File m1 = new File("song.mp3", 5000);
        File m2 = new File("beat.wav", 8000);
        docs.addComponent(f1);
        docs.addComponent(f2);
        music.addComponent(m1);
        music.addComponent(m2);
        root.addComponent(docs);
        root.addComponent(music);
        root.display("");
        System.out.println("Жалпы көлем: " + root.getSize() + "KB");
    }
}
