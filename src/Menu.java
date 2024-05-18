import java.util.Scanner;

public class Menu {
    private GestorDB gestor;

    public Menu() {
        try {
            gestor = new GestorDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Select All");
            System.out.println("2. Nitbus");
            System.out.println("3. M19");
            System.out.println("4. ???");
            System.out.println("5. Inserir");
            System.out.println("6. Modificar");
            System.out.println("7. Eliminar");
            System.out.println("8. Sortir");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            try {
                switch (choice) {
                    case 1:
                        gestor.consulta1();
                        break;
                    case 2:
                        gestor.consulta2();
                        break;
                    case 3:
                        gestor.consulta3();
                        break;
                    case 4:
                        gestor.consulta4();
                        break;
                    case 5:
                        System.out.println("Introduir dades XML a modificar:");
                        String xmlData = scanner.nextLine();
                        gestor.modificar(xmlData);
                        break;
                    case 6:
                        System.out.println("Introduir dades XML a modificar:");
                        xmlData = scanner.nextLine();
                        gestor.modificar(xmlData);
                        break;
                    case 7:
                        System.out.println("Introduir condició per eliminar:");
                        String condition = scanner.nextLine();
                        gestor.eliminar(condition);
                        break;
                    case 8:
                        gestor.close();
                        scanner.close();
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
