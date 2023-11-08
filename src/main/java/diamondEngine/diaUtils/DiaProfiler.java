package diamondEngine.diaUtils;

import java.util.HashMap;

public class DiaProfiler {

    // ATTRIBUTES
    private float registerUpdateFrequency;
    private float timePassed;
    private final HashMap<String, Float> registers;
    private final HashMap<String, Float> registerMeasurements;
    private final HashMap<String, Long> registerCurrentMeasurements;
    private final HashMap<String, Integer> registerCounts;

    // CONSTRUCTORS

    public DiaProfiler() {
        this.registerUpdateFrequency = 1f;
        this.timePassed = 0f;
        this.registerMeasurements = new HashMap<>();
        this.registers = new HashMap<>();
        this.registerCurrentMeasurements = new HashMap<>();
        this.registerCounts = new HashMap<>();
    }

    public DiaProfiler(int registerUpdateFrequency) {
        this.registerUpdateFrequency = 1f / registerUpdateFrequency;
        this.timePassed = 0f;
        this.registers = new HashMap<>();
        this.registerMeasurements = new HashMap<>();
        this.registerCurrentMeasurements = new HashMap<>();
        this.registerCounts = new HashMap<>();
    }

    // GETTERS & SETTERS
    public HashMap<String, Float> getRegisters() {
        return registers;
    }

    public void setRegisterUpdateFrequency(int registerUpdateFrequency) {
        this.registerUpdateFrequency = 1f / registerUpdateFrequency;
    }

    // METHODS
    public void addRegister(String register) {
        if (this.registerMeasurements.get(register) == null) {
            this.registers.put(register, 0f);
            this.registerMeasurements.put(register, 0f);
            this.registerCounts.put(register, 0);
            this.registerCurrentMeasurements.put(register, 0L);
        } else {
            DiaLogger.log(this.getClass(), "Register '" + register + "' already exists", DiaLoggerLevel.WARN);
        }
    }

    public void beginMeasurement(String register) {
        if (this.registers.get(register) != null) {
            this.registerCurrentMeasurements.put(register, System.nanoTime());
        }
    }

    public void endMeasurement(String register) {
        if (this.registers.get(register) != null) {
            int count = this.registerCounts.get(register);
            long elapsedNano = System.nanoTime() - this.registerCurrentMeasurements.get(register);
            this.registerMeasurements.put(register, this.registerMeasurements.get(register) + (elapsedNano / 1000000f)); // We store milliseconds
            this.registerCounts.put(register, count + 1);
        }
    }

    public void removeRegister(String register) {
        if (this.registers.get(register) != null) {
            this.registers.remove(register);
            this.registerMeasurements.remove(register);
            this.registerCurrentMeasurements.remove(register);
            this.registerCounts.remove(register);
        }
    }

    public void update(float dt) {
        timePassed += dt;
        if (timePassed >= registerUpdateFrequency) {
            for (String register : this.registers.keySet()) {
                float data = this.registerMeasurements.get(register);
                int count = this.registerCounts.get(register);
                this.registers.put(register, data / count);
                this.registerMeasurements.put(register, 0f);
                this.registerCounts.put(register, 0);
            }
            timePassed -= registerUpdateFrequency;
        }
    }
}
