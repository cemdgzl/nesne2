import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

// === MAIN CLASS ===
public class ClinicApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClinicService service = new ClinicServiceImpl();

    public static void main(String[] args) {

        boolean run = true;

        while (run) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addPatient();
                    break;
                case "2":
                    addDoctor();
                    break;
                case "3":
                    createAppointment();
                    break;
                case "4":
                    listPatients();
                    break;
                case "5":
                    listDoctors();
                    break;
                case "6":
                    listAppointments();
                    break;
                case "7":
                    listBranches();
                    break;
                case "0":
                    run = false;
                    System.out.println("Program sonlandırıldı.");
                    break;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== CLINIC SYSTEM =====");
        System.out.println("1 - Add Patient");
        System.out.println("2 - Add Doctor");
        System.out.println("3 - Create Appointment");
        System.out.println("4 - List Patients");
        System.out.println("5 - List Doctors");
        System.out.println("6 - List Appointments");
        System.out.println("7 - List Doctor Branches");
        System.out.println("0 - Exit");
        System.out.print("Choice: ");
    }

    private static void addPatient() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("National ID: ");
        String nid = scanner.nextLine();
        System.out.print("Blood Type: ");
        String blood = scanner.nextLine();

        Patient p = service.registerPatient(name, phone, nid, blood);
        System.out.println("Added patient: " + p);
    }

    private static void addDoctor() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Branch: ");
        String branch = scanner.nextLine();

        Doctor d = service.registerDoctor(name, phone, branch);
        System.out.println("Added doctor: " + d);
    }

    private static void createAppointment() {
        try {
            System.out.print("Patient ID: ");
            int pId = Integer.parseInt(scanner.nextLine());

            System.out.print("Doctor ID: ");
            int dId = Integer.parseInt(scanner.nextLine());

            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Time (HH:MM): ");
            LocalTime time = LocalTime.parse(scanner.nextLine());

            System.out.print("Note: ");
            String note = scanner.nextLine();

            LocalDateTime dt = LocalDateTime.of(date, time);

            Appointment a = service.createAppointment(pId, dId, dt, note);
            System.out.println("Created appointment: " + a);
        } catch (Exception e) {
            System.out.println("Error creating appointment: " + e.getMessage());
        }
    }

    private static void listPatients() {
        System.out.println("\n--- Patients ---");
        PrintUtil.printList(service.listPatients());
    }

    private static void listDoctors() {
        System.out.println("\n--- Doctors ---");
        PrintUtil.printList(service.listDoctors());
    }

    private static void listAppointments() {
        System.out.println("\n--- Appointments ---");
        PrintUtil.printList(service.listAppointments());
    }

    private static void listBranches() {
        System.out.println("\n--- Doctor Branches ---");
        service.listDoctorBranches()
                .forEach(b -> System.out.println("- " + b)); // lambda
    }
}

// === ABSTRACT SUPER CLASS ===
abstract class Person {
    protected int id;
    protected String name;
    protected String phone;

    public Person(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "ID=" + id + ", Name=" + name + ", Phone=" + phone;
    }
}

// === SUBCLASSES ===
class Patient extends Person {
    private String nationalId;
    private String bloodType;

    public Patient(int id, String name, String phone, String nationalId, String bloodType) {
        super(id, name, phone);
        this.nationalId = nationalId;
        this.bloodType = bloodType;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getBloodType() {
        return bloodType;
    }

    @Override
    public String toString() {
        return "Patient{" +
                super.toString() +
                ", NationalId=" + nationalId +
                ", BloodType=" + bloodType +
                '}';
    }
}

class Doctor extends Person {
    private String branch;

    public Doctor(int id, String name, String phone, String branch) {
        super(id, name, phone);
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                super.toString() +
                ", Branch=" + branch +
                '}';
    }
}

// === APPOINTMENT ===
class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime dateTime;
    private String note;

    public Appointment(int id, Patient patient, Doctor doctor, LocalDateTime dateTime, String note) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "ID=" + id +
                ", Patient=" + patient.getName() +
                ", Doctor=" + doctor.getName() +
                ", DateTime=" + dateTime +
                ", Note='" + note + '\'' +
                '}';
    }
}

// === GENERIC REPOSITORY INTERFACE ===
interface Repository<T, ID> {
    void save(ID id, T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
}

// === GENERIC REPOSITORY IMPLEMENTATION ===
class InMemoryRepository<T, ID> implements Repository<T, ID> {

    private final Map<ID, T> store = new HashMap<>();

    @Override
    public void save(ID id, T entity) {
        store.put(id, entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        // Stream kullanmadan basit ve temiz:
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(ID id) {
        store.remove(id);
    }
}

// === SERVICE INTERFACE ===
interface ClinicService {

    Patient registerPatient(String name, String phone, String nationalId, String bloodType);

    Doctor registerDoctor(String name, String phone, String branch);

    Appointment createAppointment(int patientId, int doctorId, LocalDateTime dateTime, String note);

    List<Patient> listPatients();

    List<Doctor> listDoctors();

    List<Appointment> listAppointments();

    Set<String> listDoctorBranches();
}

// === SERVICE IMPLEMENTATION (BUSINESS LAYER) ===
class ClinicServiceImpl implements ClinicService {

    private final Repository<Patient, Integer> patientRepo = new InMemoryRepository<>();
    private final Repository<Doctor, Integer> doctorRepo = new InMemoryRepository<>();
    private final Repository<Appointment, Integer> appointmentRepo = new InMemoryRepository<>();

    private int patientIdSeq = 1;
    private int doctorIdSeq = 1;
    private int appointmentIdSeq = 1;

    private final Set<String> branches = new HashSet<>();

    @Override
    public Patient registerPatient(String name, String phone, String nationalId, String bloodType) {
        Patient p = new Patient(patientIdSeq, name, phone, nationalId, bloodType);
        patientRepo.save(patientIdSeq, p);
        patientIdSeq++;
        return p;
    }

    @Override
    public Doctor registerDoctor(String name, String phone, String branch) {
        Doctor d = new Doctor(doctorIdSeq, name, phone, branch);
        doctorRepo.save(doctorIdSeq, d);
        doctorIdSeq++;
        branches.add(branch);
        return d;
    }

    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime dateTime, String note) {
        Patient p = patientRepo.findById(patientId).orElse(null);
        Doctor d = doctorRepo.findById(doctorId).orElse(null);

        if (p == null) {
            throw new IllegalArgumentException("Patient not found: " + patientId);
        }
        if (d == null) {
            throw new IllegalArgumentException("Doctor not found: " + doctorId);
        }

        Appointment a = new Appointment(appointmentIdSeq, p, d, dateTime, note);
        appointmentRepo.save(appointmentIdSeq, a);
        appointmentIdSeq++;
        return a;
    }

    @Override
    public List<Patient> listPatients() {
        return patientRepo.findAll();
    }

    @Override
    public List<Doctor> listDoctors() {
        return doctorRepo.findAll();
    }

    @Override
    public List<Appointment> listAppointments() {
        return appointmentRepo.findAll();
    }

    @Override
    public Set<String> listDoctorBranches() {
        return branches;
    }
}

// === GENERIC METHOD + LAMBDA ===
class PrintUtil {

    public static <T> void printList(List<T> list) {
        list.forEach(item -> System.out.println(item)); // lambda
    }
}
