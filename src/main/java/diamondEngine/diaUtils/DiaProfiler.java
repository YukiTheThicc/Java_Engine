package diamondEngine.diaUtils;

import java.util.HashMap;

public class DiaProfiler {

    // ATTRIBUTES
    private float registerUpdateFrequency;
    private float timePassed;
    private HashMap<String, float[]> registers;

    // CONSTRUCTORS
    public DiaProfiler() {
        this.registerUpdateFrequency = 1f;
        this.timePassed = 0f;
        this.registers = new HashMap<>();
    }

    public DiaProfiler(float registerUpdateFrequency) {
        this.registerUpdateFrequency = registerUpdateFrequency;
        this.timePassed = 0f;
        this.registers = new HashMap<>();
    }

    // GETTERS & SETTERS
    public float getRegisterUpdateFrequency() {
        return registerUpdateFrequency;
    }

    public float getTimePassed() {
        return timePassed;
    }

    public HashMap<String, float[]> getRegisters() {
        return registers;
    }

    public void setRegisterUpdateFrequency(float registerUpdateFrequency) {
        this.registerUpdateFrequency = registerUpdateFrequency;
    }

    public void setTimePassed(float timePassed) {
        this.timePassed = timePassed;
    }

    public void setRegisters(HashMap<String, float[]> registers) {
        this.registers = registers;
    }

    // METHODS
    public void addRegister(String register) {
        if (this.registers.get(register) != null) {
            this.registers.put(register, new float[64]);
        } else {
            DiaLogger.log(this.getClass(), "Register '" + register + "' already exists", DiaLoggerLevel.WARN);
        }
    }

    public void addMeasurement(String register, float data) {

    }

    public void update(float dt) {
        timePassed += dt;
        if (timePassed >= registerUpdateFrequency) {

            timePassed -= registerUpdateFrequency;
        }
    }
}
