package m1.uasz.sn.ui.components;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.dao.NoteDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.services.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotePanel extends JPanel {
    private JComboBox<String> formationDropdown;
    private JComboBox<String> moduleDropdown;
    private JTable table;
    private DefaultTableModel model;
    private EtudiantService etudiantService = new EtudiantService();
    private ModuleService moduleService = new ModuleService();
    private ModuleDAO moduleDAO = new ModuleDAO();
    private FormationService formationService = new FormationService();
    private NoteDAO noteDAO = new NoteDAO();
    private Map<String, Long> formationsMap = new HashMap<>();
    private Map<String, Long> modulesMap = new HashMap<>();

    public NotePanel() {
        FlatLightLaf.setup(); // Activation du FlatLaf pour un look moderne
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titreLabel = new JLabel("Gestion des Notes", SwingConstants.CENTER);
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(new Color(50, 50, 50));
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel dropdownPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        formationDropdown = new JComboBox<>();
        moduleDropdown = new JComboBox<>();
        JButton validerButton = new JButton("Afficher les Notes");

        formationDropdown.addActionListener(e -> updateModules());
        validerButton.addActionListener(e -> loadNotes());

        dropdownPanel.add(new JLabel("Formation:"));
        dropdownPanel.add(formationDropdown);
        dropdownPanel.add(new JLabel("Module:"));
        dropdownPanel.add(moduleDropdown);
        dropdownPanel.add(validerButton);

        String[] columns = {"INE", "Nom", "Prénom", "Contrôle Continu", "Examen"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(new Color(200, 200, 200));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));

        JScrollPane tablePanel = new JScrollPane(table);

        add(titreLabel, BorderLayout.NORTH);
        add(dropdownPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        loadFormations();
    }

    private void loadFormations() {
        List<Formation> formations = formationService.listerFormations();
        formationDropdown.removeAllItems();
        formationsMap.clear();

        for (Formation formation : formations) {
            formationDropdown.addItem(formation.getNom());
            formationsMap.put(formation.getNom(), formation.getId());
        }
    }

    private void updateModules() {
        String selectedFormationNom = (String) formationDropdown.getSelectedItem();
        if (selectedFormationNom != null && formationsMap.containsKey(selectedFormationNom)) {
            moduleDropdown.removeAllItems();
            modulesMap.clear();

            Formation idFormation = formationService.trouverFormation(formationsMap.get(selectedFormationNom));
            List<Module> modules = moduleDAO.findModulesByFormation(idFormation);

            for (Module module : modules) {
                moduleDropdown.addItem(module.getNom());
                modulesMap.put(module.getNom(), module.getId());
            }
        }
    }

    private void loadNotes() {
        String selectedModuleNom = (String) moduleDropdown.getSelectedItem();
        if (selectedModuleNom != null && modulesMap.containsKey(selectedModuleNom)) {
            Module moduleId = moduleService.trouverModule(modulesMap.get(selectedModuleNom));
            List<Etudiant> etudiants = moduleDAO.findEtudiantsByModules(moduleId);
            model.setRowCount(0);

            for (Etudiant etudiant : etudiants) {
                Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, moduleId);
                double noteCC = (note != null) ? note.getNoteCC() : 0.0;
                double noteExam = (note != null) ? note.getNoteExamen() : 0.0;

                model.addRow(new Object[]{
                        etudiant.getIne(),
                        etudiant.getNom(),
                        etudiant.getPrenoms(),
                        noteCC,
                        noteExam
                });
            }
        }
    }
}
