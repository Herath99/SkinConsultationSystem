import java.util.*;
import java.io.*;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.List;
import Classes.*;
import util.EncryptionUtil;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WestminsterSkinConsultationManager implements SkinConsultationManager{
    private static final int MAX_DOCTORS = 10;         // Maximum number of doctors that can be added to the program
    private List<Doctor> doctors;                      // List of Classes.Doctor objects
    private Scanner scanner;                           // Scanner to read user input
    ArrayList<Consultation> consultationList = new ArrayList<>();

    public static void main(String[] args) {
        WestminsterSkinConsultationManager manager = new WestminsterSkinConsultationManager();  // Create a WestminsterSkinConsultationManager object
        manager.run();                                                                          // Run the manager's main loop
    }


    public WestminsterSkinConsultationManager() {
        doctors = new ArrayList<>();                                            // Initialize the list of doctors
        scanner = new Scanner(System.in);                                       // Initialize the Scanner to read user input
    }

    public void run() {                                     // Main loop of the program
        while (true) {
            printMenu();                                    // Print the menu and get the user's choice
            int choice = scanner.nextInt();
            switch (choice) {                               // Perform the chosen action
                case 1:
                    addDoctor();                            // Add a doctor
                    break;
                case 2:
                    deleteDoctor();                         // Delete a doctor
                    break;
                case 3:
                    printDoctors();                         // Print the list of doctors
                    break;
                case 4:
                    saveInformation();                      // Save the list of doctors to a file
                    break;
                case 5:
                    addConsultation();                      // Add a consultation in GUI
                    break;
                case 6:
                    readInformation();                      // Read the list of doctors from a file
                    break;
                case 7:
                    exit();                                 // Exit the program
                    break;
                default:
                    System.out.println("Invalid choice.");  // Invalid choice
                    break;
            }
        }
    }

    public void printMenu() {                                                      // Print the menu options
        System.out.println("\n--- Menu ---\n");
        System.out.println("1. Add doctor");
        System.out.println("2. Delete doctor");
        System.out.println("3. Print doctors");
        System.out.println("4. Save information");
        System.out.println("5. Consult a doctor");
        System.out.println("6. read information");
        System.out.println("7. Exit");
        System.out.println("\nSelect an option:\n");

    }

    public void addDoctor() {
        if (doctors.size() >= MAX_DOCTORS) {                                        // Check if the maximum number of doctors has been reached
            System.out.println("Error: Maximum number of doctors reached.");
            return;
        }
        try {                                                                       // Get the doctor's information from the user
            System.out.print("Enter name: ");
            String name = scanner.next();
            System.out.print("Enter surname: ");
            String surname = scanner.next();
            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            String dobStr = scanner.next();
            LocalDate dob = LocalDate.parse(dobStr);
            System.out.print("Enter mobile number: ");
            String mobileNumber = scanner.next();
            System.out.print("Enter medical license number: ");
            String medicalLicenseNumber = scanner.next();
            System.out.print("Enter speciality(e.g. cosmetic dermatology, medical dermatology, paediatricdermatology, etc.): ");
            String speciality = scanner.next();

            Doctor doctor = new Doctor(name, surname, dob, mobileNumber, medicalLicenseNumber, speciality);     // Create a new Classes.Doctor object with the entered information
            doctors.add(doctor);                                                                                // Add the doctor to the list
            System.out.println("\nClasses.Doctor added successfully.");
        } catch (DateTimeParseException e) {                                                                    // Invalid date format
            System.out.println("\nError: Invalid date format.");
        }
    }

    public void deleteDoctor() {
        try {
            System.out.print("\nEnter medical license number: ");       // Get the medical license number of the doctor to delete
            String medicalLicenseNumber = scanner.next();               // Prompt user to enter medical license number
            int index = -1;                                             // Find doctor with specified medical license number
            for (int i = 0; i < doctors.size(); i++) {
                Doctor doctor = doctors.get(i);
                if (doctor.getMedicalLicenseNumber().equals(medicalLicenseNumber)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {                                          // If no doctor is found, throw an exception
                throw new IllegalArgumentException("\nError: Classes.Doctor with medical license number " + medicalLicenseNumber + " not found.");
            }
            Doctor deletedDoctor = doctors.remove(index);               // Remove doctor from list and print their information
            System.out.println("\nClasses.Doctor deleted successfully:");
            System.out.println("\nName: " + deletedDoctor.getName());
            System.out.println("Surname: " + deletedDoctor.getSurname());
            System.out.println("Medical license number: " + deletedDoctor.getMedicalLicenseNumber());
            System.out.println("Speciality: " + deletedDoctor.getSpeciality());
            System.out.println("Total number of doctors: " + doctors.size());
        } catch (IllegalArgumentException e) {                          // If an exception is thrown, print the error message
            System.out.println(e.getMessage());
        }
    }

    public void printDoctors() {                                       // Prints a list of all doctors in the system
        try {
            if (doctors.isEmpty()) {
                throw new IllegalStateException("Error: No doctors found.");    // Throws an IllegalStateException if there are no doctors in the system
            }
            Collections.sort(doctors, (d1, d2) -> d1.getSurname().compareTo(d2.getSurname()));      // Sorts the list of doctors by their surname
            for (Doctor doctor : doctors) {                                                         // Prints the details of each doctor in the list
                System.out.println("\nName: " + doctor.getName());
                System.out.println("Surname: " + doctor.getSurname());
                System.out.println("Date of birth: " + doctor.getDob().toString());
                System.out.println("Mobile number: " + doctor.getMobileNumber());
                System.out.println("Medical license number: " + doctor.getMedicalLicenseNumber());
                System.out.println("Speciality: " + doctor.getSpeciality());
            }
            System.out.println("\nClasses.Doctor list printed successfully");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveInformation() {                                    // Method to save the information of all doctors to a file called "doctors.txt"
        try (FileWriter fw = new FileWriter("doctors.txt")) {
            for (Doctor doctor : doctors) {                             // Iterate through the list of doctors and write their information to the file
                String line = doctor.getName() + "," + doctor.getSurname() + "," + doctor.getDob().toString() + "," + doctor.getMobileNumber() + "," + doctor.getMedicalLicenseNumber() + "," + doctor.getSpeciality() + "\n";
                fw.write(line);
            }
            System.out.println("Information saved successfully.");      // Print a success message
        } catch (IOException e) {
            System.out.println("Error: Could not save information.");   // Print an error message if an exception occurs while writing to the file
        }
    }

    public void readInformation() {                                    // Method to read the information of all doctors from a file called "doctors.txt"
        try (FileReader fr = new FileReader("doctors.txt"); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {                    // Read the file line by line until there are no more lines to read
                String[] fields = line.split(",");                // Split the line on the comma delimiter to get the individual fields
                String name = fields[0];
                String surname = fields[1];
                LocalDate dob = LocalDate.parse(fields[2]);
                String mobileNumber = fields[3];
                String medicalLicenseNumber = fields[4];
                String speciality = fields[5];

                Doctor doctor = new Doctor(name, surname, dob, mobileNumber, medicalLicenseNumber, speciality);     // Create a new Classes.Doctor object with the fields read from the file
                doctors.add(doctor);                                                                                // Add the doctor to the list of doctors
            }
            if(doctors.isEmpty()) {                                                                                 // Check if the list of doctors is empty
                System.out.println("Error: No doctors were saved to the file");
            }
            else {
                if(!doctors.isEmpty()){                                                                             // Print a success message if the list is not empty
                    System.out.println("\nClasses.Doctor file created successfully.");
                }
            }
        } catch (IOException | DateTimeParseException e) {
            System.out.println("Error: Could not read information.");                      // Print an error message if an exception occurs while reading the file
        }
    }

    public void addConsultation() {

        final JFrame frame = new JFrame("Skin_Consultation_Center");

        String[] columns = {"Name", "Medical License Number" , "Surname" , "Date of Birth", "Mobile Number", "Specialisation"};

        Object[][] obj = new Object[doctors.size()][6];
        int i = 0;
        if (doctors.size() != 0) {
            for (Doctor doc : doctors) {
                obj[i][0] = doc.getName();
                obj[i][1] = doc.getMedicalLicenseNumber();
                obj[i][2] = doc.getSurname();
                obj[i][3] = doc.getDob();
                obj[i][4] = doc.getMobileNumber();
                obj[i][5] = doc.getSpeciality();
                i++;
            }
        }

        JTable table = new JTable(obj, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JButton ConsultADoctorButton = new JButton("Consult a doctor");
        JButton AddedConsultationButton = new JButton("Added Consultation");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(ConsultADoctorButton);
        buttonPanel.add(AddedConsultationButton);

        ConsultADoctorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();

                if (row != -1) {
                    String medicalLicenceNumber = table.getModel().getValueAt(row, 0).toString();
                    String name = table.getModel().getValueAt(row, 1).toString();
                    String surname = table.getModel().getValueAt(row, 2).toString();
                    String dob = table.getModel().getValueAt(row, 3).toString();
                    String mobile = table.getModel().getValueAt(row, 4).toString();
                    String specialisation = table.getModel().getValueAt(row, 5).toString();

                    Doctor doctor = new Doctor(name, surname, LocalDate.parse(dob), mobile, String.valueOf(medicalLicenceNumber), specialisation);

                    JFrame patientDetailsFrame = new JFrame();
                    JPanel patientDetailsPanel = new JPanel();
                    JPanel patientNotePanel = new JPanel();
                    JPanel patientButtonPanel = new JPanel();

                    JPanel panelDOB = new JPanel();
                    JPanel panelConsultationDate = new JPanel();

                    JLabel labelName = new JLabel("Enter your Name");
                    JLabel labelSurname = new JLabel("Enter your Surname");
                    JLabel labelDob = new JLabel("Enter Your Date of birth");
                    JLabel labelMobile = new JLabel("Enter Your Mobile number");
                    JLabel labelConsultationDate = new JLabel("Add Your Consultation date");
                    JLabel labelConsultationTime = new JLabel("Add Your Consultation time");

                    JTextField textFieldName = new JTextField();
                    JTextField textFieldSurname = new JTextField();
                    JTextField textFieldMobile = new JTextField();

                    String[] days = {"Day", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
                    String[] months = {"Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
                    String[] yearsForDob = {"Year", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013"};
                    String[] yearsForConsultation = {"Year", "2023", "2024"};

                    JComboBox comboBoxdobDate = new JComboBox(days);
                    JComboBox comboBoxdobMonth = new JComboBox(months);
                    JComboBox comboBoxdobYear = new JComboBox(yearsForDob);
                    panelDOB.setLayout(new GridLayout(1, 3));
                    panelDOB.add(comboBoxdobDate);
                    panelDOB.add(comboBoxdobMonth);
                    panelDOB.add(comboBoxdobYear);


                    JComboBox comboBoxConsultationDate = new JComboBox(days);
                    JComboBox comboBoxconsultationMonth = new JComboBox(months);
                    JComboBox comboBoxconsultationYear = new JComboBox(yearsForConsultation);
                    panelConsultationDate.setLayout(new GridLayout(1, 3));
                    panelConsultationDate.add(comboBoxConsultationDate);
                    panelConsultationDate.add(comboBoxconsultationMonth);
                    panelConsultationDate.add(comboBoxconsultationYear);

                    String[] petStrings = {"Select a Time Slot", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00"};
                    JComboBox comboBoxConsultationTime = new JComboBox(petStrings);


                    patientDetailsPanel.add(labelName, BorderLayout.CENTER);
                    patientDetailsPanel.add(textFieldName, BorderLayout.CENTER);

                    patientDetailsPanel.add(labelSurname, BorderLayout.CENTER);
                    patientDetailsPanel.add(textFieldSurname, BorderLayout.CENTER);

                    patientDetailsPanel.add(labelDob, BorderLayout.CENTER);
                    patientDetailsPanel.add(panelDOB, BorderLayout.CENTER);

                    patientDetailsPanel.add(labelMobile, BorderLayout.CENTER);
                    patientDetailsPanel.add(textFieldMobile, BorderLayout.CENTER);

                    patientDetailsPanel.add(labelConsultationDate, BorderLayout.CENTER);
                    patientDetailsPanel.add(panelConsultationDate, BorderLayout.CENTER);

                    patientDetailsPanel.add(labelConsultationTime, BorderLayout.CENTER);
                    patientDetailsPanel.add(comboBoxConsultationTime, BorderLayout.CENTER);

                    patientDetailsPanel.setLayout(new GridLayout(6, 2));
                    patientDetailsPanel.setBounds(30, 40, 540, 180);
                    patientDetailsPanel.setBackground(Color.ORANGE);


                    JTextArea textAreaNote = new JTextArea();
                    textAreaNote.setColumns(40);
                    textAreaNote.setLineWrap(true);
                    textAreaNote.setRows(5);
                    textAreaNote.setWrapStyleWord(true);
                    textAreaNote.setBounds(0, 0, 540, 50);
                    textAreaNote.setToolTipText("Add a note here");

                    patientNotePanel.setBounds(30, 230, 540, 50);
                    patientNotePanel.add(textAreaNote);


                    JButton saveConsultationButton = new JButton("Done");

                    saveConsultationButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {


                            String patientName = textFieldName.getText();
                            String patientSurname = textFieldSurname.getText();

                            Object dd = comboBoxdobDate.getSelectedItem();
                            Object dm = comboBoxdobMonth.getSelectedItem();
                            Object dy = comboBoxdobYear.getSelectedItem();

                            String patientMobileNumber = textFieldMobile.getText();

                            Object cd = comboBoxConsultationDate.getSelectedItem();
                            Object cm = comboBoxconsultationMonth.getSelectedItem();
                            Object cy = comboBoxconsultationYear.getSelectedItem();

                            Object patientTime = comboBoxConsultationTime.getSelectedItem();

                            String note = textAreaNote.getText();

                            if (patientName != null && !patientName.trim().equalsIgnoreCase("")) {
                                if (patientSurname != null && !patientSurname.trim().equalsIgnoreCase("")) {
                                    if (patientMobileNumber != null && !patientMobileNumber.trim().equalsIgnoreCase("")) {
                                        if (note != null && !note.trim().equalsIgnoreCase("")) {

                                            LocalDate dob = LocalDate.parse(dy + "-" + dm + "-" + dd);
                                            LocalDate consultationDate = LocalDate.parse(cy + "-" + cm + "-" + cd);

                                            try {
                                                SecretKey key = EncryptionUtil.generateKey(128);
                                                IvParameterSpec ivParameterSpec = EncryptionUtil.generateIv();
                                                String algorithm = "AES/CBC/PKCS5Padding";
                                                String cipherText = EncryptionUtil.encrypt(algorithm, note, key, ivParameterSpec);

                                                String uniqueID = UUID.randomUUID().toString();

                                                if (consultationList.isEmpty()) {

                                                    Patient patient = new Patient(patientName, patientSurname, dob, patientMobileNumber, uniqueID);

                                                    Consultation consultation = new Consultation(consultationDate, String.valueOf(patientTime), cipherText, doctor, patient);
                                                    consultationList.add(consultation);

                                                    JOptionPane.showMessageDialog(patientDetailsFrame, "Successful. Your cost is £" + 25 + "");

                                                } else {

                                                    boolean alreadyBook = false;

                                                    for (Consultation c : consultationList) {
                                                        if (String.valueOf(patientTime) == c.getTime() && consultationDate.isEqual(c.getDate())
                                                                && doctor.getMedicalLicenseNumber() == c.getDoctor().getMedicalLicenseNumber()) {
                                                            alreadyBook = true;
                                                            break;
                                                        }
                                                    }

                                                    if (alreadyBook) {

                                                        Doctor randomDoctor = findRandomDoctor(doctor, String.valueOf(patientTime), consultationDate);

                                                        if (randomDoctor != null) {
                                                            Patient patient = new Patient(patientName, patientSurname, dob, patientMobileNumber, uniqueID);

                                                            Consultation consultation = new Consultation(consultationDate, String.valueOf(patientTime), cipherText, randomDoctor, patient);
                                                            consultationList.add(consultation);
                                                            JOptionPane.showMessageDialog(patientDetailsFrame, "Successful. Your cost is £" + 25 + "");
                                                        } else {
                                                            JOptionPane.showMessageDialog(patientDetailsFrame, "No any other doctors found for selected time slot and date.");
                                                        }

                                                    } else {
                                                        Patient patient = new Patient(patientName, patientSurname, dob, patientMobileNumber, uniqueID);

                                                        Consultation consultation = new Consultation(consultationDate, String.valueOf(patientTime), cipherText, doctor, patient);
                                                        consultationList.add(consultation);
                                                        JOptionPane.showMessageDialog(patientDetailsFrame, "Successful. Your cost is £" + 25 + "");
                                                    }
                                                }

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                        } else {
                                            JOptionPane.showMessageDialog(patientDetailsFrame, "Please enter a note.");
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(patientDetailsFrame, "Please enter patient mobile no.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(patientDetailsFrame, "Please enter patient surname.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(patientDetailsFrame, "Please enter patient name.");
                            }
                        }
                    });

                    patientButtonPanel.setBounds(30, 290, 540, 50);
                    patientButtonPanel.setBackground(Color.GREEN);
                    patientButtonPanel.add(saveConsultationButton);

                    patientDetailsFrame.add(patientDetailsPanel);
                    patientDetailsFrame.add(patientButtonPanel);
                    patientDetailsFrame.add(patientNotePanel);

                    patientDetailsFrame.setSize(600, 500);
                    patientDetailsFrame.setLayout(null);
                    patientDetailsFrame.setVisible(true);
                    patientDetailsFrame.setLocationRelativeTo(null);


                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a doctor.");
                }

            }
        });

        AddedConsultationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(!consultationList.isEmpty()){

                    final JFrame consultationViewFrame = new JFrame("Skin Consultation Center");

                    String[] consultationColumns = {"Doctor's name","MedicalLicence No","Specialisation", "Patient name", "Date", "Time", "Note"};

                    Object[][] consultationObject = new Object[consultationList.size()][7];
                    int i = 0;
                    for (Consultation consultation : consultationList) {
                        consultationObject[i][0] = consultation.getDoctor().getMedicalLicenseNumber();
                        consultationObject[i][1] = consultation.getDoctor().getName();
                        consultationObject[i][2] = consultation.getDoctor().getSpeciality();
                        consultationObject[i][3] = consultation.getPatient().getName();
                        consultationObject[i][4] = consultation.getDate();
                        consultationObject[i][5] = consultation.getTime();
                        consultationObject[i][6] = consultation.getNotes();
                        i++;
                    }

                    JTable consultationTable = new JTable(consultationObject, consultationColumns);
                    JScrollPane consultationScrollPane = new JScrollPane(consultationTable);
                    table.setFillsViewportHeight(true);


                    consultationViewFrame.add(consultationScrollPane, BorderLayout.CENTER);
                    consultationViewFrame.setLayout(new GridLayout(1, 1));
                    consultationViewFrame.setSize(600, 400);
                    consultationViewFrame.setVisible(true);
                    consultationViewFrame.setLocationRelativeTo(null);



                }else{
                    JOptionPane.showMessageDialog(frame, "No consultation found.");
                }

            }
        });


        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setLayout(new GridLayout(2, 1));
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    private Doctor findRandomDoctor(Doctor userSelectedDoctor, String patientTimeSlot, LocalDate consultationDate) {
        boolean foundRandomDoctor = false;
        Doctor randomDoctor = null;
        Random rand = new Random();

        while (true) {
            randomDoctor = doctors.get(rand.nextInt(doctors.size()));

            if (randomDoctor.getMedicalLicenseNumber() == userSelectedDoctor.getMedicalLicenseNumber()) {
                continue;
            } else {
                if (!consultationList.isEmpty()) {
                    for (Consultation c : consultationList) {
                        if (patientTimeSlot == c.getTime() && consultationDate.isEqual(c.getDate())
                                && randomDoctor.getMedicalLicenseNumber() == c.getDoctor().getMedicalLicenseNumber()) {
                            continue;
                        } else {
                            return randomDoctor;
                        }
                    }
                } else {
                    return randomDoctor;
                }

            }
        }
    }

    public void exit() {
        System.out.println("Exiting Menu....");
        System.exit(1);
    }
}
