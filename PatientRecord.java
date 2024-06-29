import javafx.beans.property.*;

public class PatientRecord {
        private final SimpleStringProperty name;
        private final SimpleStringProperty date;
        private final SimpleDoubleProperty sphereRight;
        private final SimpleDoubleProperty sphereLeft;
        private final SimpleDoubleProperty cylinderRight;
        private final SimpleDoubleProperty cylinderLeft;

        public PatientRecord(String name, String date, double sphereRight, double sphereLeft, double cylinderRight, double cylinderLeft) {
            this.name = new SimpleStringProperty(name);
            this.date = new SimpleStringProperty(date);
            this.sphereRight = new SimpleDoubleProperty(sphereRight);
            this.sphereLeft = new SimpleDoubleProperty(sphereLeft);
            this.cylinderRight = new SimpleDoubleProperty(cylinderRight);
            this.cylinderLeft = new SimpleDoubleProperty(cylinderLeft);
            
            
            
        }
        
        public void setName(String name) {
            this.name.set(name);
        }
        
        public void setDate(String date) {
            this.date.set(date);
        }
        
        public void setSphereRight(double sphereRight) {
            this.sphereRight.set(sphereRight);
        }
        
        public void setSphereLeft(double sphereLeft) {
            this.sphereLeft.set(sphereLeft);
        }
        
        public void setCylinderRight(double cylinderRight) {
            this.cylinderRight.set(cylinderRight);
        }
        
        public void setCylinderLeft(double cylinderLeft) {
            this.cylinderLeft.set(cylinderLeft);
        }
        
        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public double getSphereRight() {
            return sphereRight.get();
        }

        public SimpleDoubleProperty sphereRightProperty() {
            return sphereRight;
        }

        public double getSphereLeft() {
            return sphereLeft.get();
        }

        public SimpleDoubleProperty sphereLeftProperty() {
            return sphereLeft;
        }

        public double getCylinderRight() {
            return cylinderRight.get();
        }

        public SimpleDoubleProperty cylinderRightProperty() {
            return cylinderRight;
        }

        public double getCylinderLeft() {
            return cylinderLeft.get();
        }

        public SimpleDoubleProperty cylinderLeftProperty() {
            return cylinderLeft;
        }
    }

