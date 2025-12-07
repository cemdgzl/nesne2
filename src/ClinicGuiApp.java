import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Basit Swing arayüzü.
 * NOT: Bu sınıfın çalışması için aynı projede şu sınıflar zaten olmalı:
 * Patient, Doctor, Appointment, Repository, InMemoryRepository,
 * ClinicService, ClinicServiceImpl, PrintUtil
 */
public class ClinicGuiApp extends JFrame {

    private final ClinicService service = new ClinicServiceImpl();

    // Ortak çıktı alanı
    private JTextArea outputArea;

    // Hasta alanları
    private JTextField patientNameField;
    private JTextField patientPhoneField;
    private JTextField patientNidField;
    private JTextField patientBloodField;

    // Doktor alanları
    private JTextField doctorNameField;
    private JTextField doctorPhoneField;
    private JTextField doctorBranchField;

    // Randevu alanları
    private JTextField appPatientIdField;
    private JTextField appDoctorIdField;
    private JTextField appDateField; // YYYY-MM-DD
    private JTextField appTimeField; // HH:MM
    private JTextField appNoteField;

    public ClinicGuiApp() {
        setTitle("Clinic Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Ana layout
        setLayout(new BorderLayout());

        // Sekmeli yapı (Hasta / Doktor / Randevu)
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Patients", createPatientPanel());
        tabbedPane.add("Doctors", createDoctorPanel());
        tabbedPane.add("Appointments", createAppointmentPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Ortak çıktı alanı
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        scrollPane.setPreferredSize(new Dimension(700, 200));

        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        patientNameField = new JTextField();
        patientPhoneField = new JTextField();
        patientNidField = new JTextField();
        patientBloodField = new JTextField();

        form.add(new JLabel("Name:"));
        form.add(patientNameField);
        form.add(new JLabel("Phone:"));
        form.add(patientPhoneField);
        form.add(new JLabel("National ID:"));
        form.add(patientNidField);
        form.add(new JLabel("Blood Type:"));
        form.add(patientBloodField);

        JButton addBtn = new JButton("Add Patient");
        addBtn.addActionListener(e -> onAddPatient());

        JButton listBtn = new JButton("List Patients");
        listBtn.addActionListener(e -> onListPatients());

        form.add(addBtn);
        form.add(listBtn);

        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        doctorNameField = new JTextField();
        doctorPhoneField = new JTextField();
        doctorBranchField = new JTextField();

        form.add(new JLabel("Name:"));
        form.add(doctorNameField);
        form.add(new JLabel("Phone:"));
        form.add(doctorPhoneField);
        form.add(new JLabel("Branch:"));
        form.add(doctorBranchField);

        JButton addBtn = new JButton("Add Doctor");
        addBtn.addActionListener(e -> onAddDoctor());

        JButton listBtn = new JButton("List Doctors");
        listBtn.addActionListener(e -> onListDoctors());

        JButton branchBtn = new JButton("List Branches");
        branchBtn.addActionListener(e -> onListBranches());

        form.add(addBtn);
        form.add(listBtn);

        JPanel bottom = new JPanel();
        bottom.add(branchBtn);

        panel.add(form, BorderLayout.NORTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        appPatientIdField = new JTextField();
        appDoctorIdField = new JTextField();
        appDateField = new JTextField("2025-01-01");
        appTimeField = new JTextField("10:00");
        appNoteField = new JTextField();

        form.add(new JLabel("Patient ID:"));
        form.add(appPatientIdField);
        form.add(new JLabel("Doctor ID:"));
        form.add(appDoctorIdField);
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(appDateField);
        form.add(new JLabel("Time (HH:MM):"));
        form.add(appTimeField);
        form.add(new JLabel("Note:"));
        form.add(appNoteField);

        JButton createBtn = new JButton("Create Appointment");
        createBtn.addActionListener(e -> onCreateAppointment());

        JButton listBtn = new JButton("List Appointments");
        listBtn.addActionListener(e -> onListAppointments());

        form.add(createBtn);
        form.add(listBtn);

        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    // === EVENT METOTLARI ===

    private void onAddPatient() {
        String name = patientNameField.getText().trim();
        String phone = patientPhoneField.getText().trim();
        String nid = patientNidField.getText().trim();
        String blood = patientBloodField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            showError("Name and Phone are required!");
            return;
        }

        Patient p = service.registerPatient(name, phone, nid, blood);
        appendOutput("Added patient: " + p);
        clearPatientFields();
    }

    private void clearPatientFields() {
        patientNameField.setText("");
        patientPhoneField.setText("");
        patientNidField.setText("");
        patientBloodField.setText("");
    }

    private void onAddDoctor() {
        String name = doctorNameField.getText().trim();
        String phone = doctorPhoneField.getText().trim();
        String branch = doctorBranchField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            showError("Name and Phone are required!");
            return;
        }

        Doctor d = service.registerDoctor(name, phone, branch);
        appendOutput("Added doctor: " + d);
        clearDoctorFields();
    }

    private void clearDoctorFields() {
        doctorNameField.setText("");
        doctorPhoneField.setText("");
        doctorBranchField.setText("");
    }

    private void onCreateAppointment() {
        try {
            int pId = Integer.parseInt(appPatientIdField.getText().trim());
            int dId = Integer.parseInt(appDoctorIdField.getText().trim());
            LocalDate date = LocalDate.parse(appDateField.getText().trim());
            LocalTime time = LocalTime.parse(appTimeField.getText().trim());
            String note = appNoteField.getText().trim();

            LocalDateTime dt = LocalDateTime.of(date, time);

            Appointment a = service.createAppointment(pId, dId, dt, note);
            appendOutput("Created appointment: " + a);
            clearAppointmentFields();
        } catch (Exception ex) {
            showError("Error creating appointment: " + ex.getMessage());
        }
    }

    private void clearAppointmentFields() {
        appPatientIdField.setText("");
        appDoctorIdField.setText("");
        // date & time alanlarını istersen boşaltmayabilirsin
        appNoteField.setText("");
    }

    private void onListPatients() {
        appendOutput("--- Patients ---");
        for (Patient p : service.listPatients()) {
            appendOutput(p.toString());
        }
    }

    private void onListDoctors() {
        appendOutput("--- Doctors ---");
        for (Doctor d : service.listDoctors()) {
            appendOutput(d.toString());
        }
    }

    private void onListAppointments() {
        appendOutput("--- Appointments ---");
        for (Appointment a : service.listAppointments()) {
            appendOutput(a.toString());
        }
    }

    private void onListBranches() {
        appendOutput("--- Doctor Branches ---");
        for (String b : service.listDoctorBranches()) {
            appendOutput("- " + b);
        }
    }

    // === YARDIMCI METOTLAR ===

    private void appendOutput(String text) {
        outputArea.append(text + "\n");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // === PROGRAM GİRİŞ NOKTASI (GUI) ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClinicGuiApp app = new ClinicGuiApp();
            app.setVisible(true);
        });
    }
}
