package pl.vtt.wpi.core.domain.model.device;

import pl.vtt.wpi.core.domain.model.Color;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public record CurrentState(
        Boolean            alive,
        LocalTime          time,
        String             waterflow,
        String             sensorPin,
        String             offPin,
        String             action,
        List<PixelProgram> programs,
        RuntimeData        data,
        List<Color>        pixels,
        Color              nextPixel,
        Integer            nol,
        Integer            restartCountdown,
        Integer            pixelProgram,
        Integer            brightness,
        Double             stepTime,
        Boolean            on,
        Boolean            sensorDependency,
        Boolean            timeDependency,
        Boolean            overflow,
        LocalTime          onTime,
        LocalTime          offTime
) {
    public CurrentState {
        if (pixels == null || pixels.isEmpty()) {
            pixels = Collections.emptyList();
        } else {
            pixels = List.copyOf(pixels);
        }
        if (programs == null || programs.isEmpty()) {
            programs = Collections.emptyList();
        } else {
            programs = List.copyOf(programs);
        }
    }

    public static class Builder {
        private Boolean            alive;
        private LocalTime          time;
        private String             waterflow;
        private String             sensorPin;
        private String             offPin;
        private String             action;
        private List<PixelProgram> programs;
        private RuntimeData        data;
        private List<Color>        pixels;
        private Color              nextPixel;
        private Integer            nol;
        private Integer            restartCountdown;
        private Integer            pixelProgram;
        private Integer            brightness;
        private Double             stepTime;
        private Boolean            on;
        private Boolean            sensorDependency;
        private Boolean            timeDependency;
        private Boolean            overflow;
        private LocalTime          onTime;
        private LocalTime          offTime;

        public Builder() {
        }

        public Builder from(CurrentState currentState) {
            this.alive = currentState.alive;
            this.time = currentState.time;
            this.waterflow = currentState.waterflow;
            this.sensorPin = currentState.sensorPin;
            this.offPin = currentState.offPin;
            this.action = currentState.action;
            this.programs = currentState.programs;
            this.data = currentState.data;
            this.pixels = currentState.pixels;
            this.nextPixel = currentState.nextPixel;
            this.nol = currentState.nol;
            this.restartCountdown = currentState.restartCountdown;
            this.pixelProgram = currentState.pixelProgram;
            this.brightness = currentState.brightness;
            this.stepTime = currentState.stepTime;
            this.on = currentState.on;
            this.sensorDependency = currentState.sensorDependency;
            this.timeDependency = currentState.timeDependency;
            this.overflow = currentState.overflow;
            this.onTime = currentState.onTime;
            this.offTime = currentState.offTime;
            return this;
        }

        public Builder alive(Boolean alive) {
            this.alive = alive;
            return this;
        }

        public Builder time(LocalTime time) {
            this.time = time;
            return this;
        }

        public Builder waterflow(String waterflow) {
            this.waterflow = waterflow;
            return this;
        }

        public Builder sensorPin(String sensorPin) {
            this.sensorPin = sensorPin;
            return this;
        }

        public Builder offPin(String offPin) {
            this.offPin = offPin;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder programs(List<PixelProgram> programs) {
            this.programs = programs == null ? Collections.emptyList() : List.copyOf(programs);
            return this;
        }

        public Builder data(RuntimeData data) {
            this.data = data;
            return this;
        }

        public Builder pixels(List<Color> pixels) {
            this.pixels = pixels == null ? Collections.emptyList() : List.copyOf(pixels);
            return this;
        }

        public Builder nextPixel(Color nextPixel) {
            this.nextPixel = nextPixel;
            return this;
        }

        public Builder nol(Integer nol) {
            this.nol = nol;
            return this;
        }

        public Builder restartCountdown(Integer restartCountdown) {
            this.restartCountdown = restartCountdown;
            return this;
        }

        public Builder pixelProgram(Integer pixelProgram) {
            this.pixelProgram = pixelProgram;
            return this;
        }

        public Builder brightness(Integer brightness) {
            this.brightness = brightness;
            return this;
        }

        public Builder stepTime(Double stepTime) {
            this.stepTime = stepTime;
            return this;
        }

        public Builder on(Boolean on) {
            this.on = on;
            return this;
        }

        public Builder sensorDependency(Boolean sensorDependency) {
            this.sensorDependency = sensorDependency;
            return this;
        }

        public Builder timeDependency(Boolean timeDependency) {
            this.timeDependency = timeDependency;
            return this;
        }

        public Builder overflow(Boolean overflow) {
            this.overflow = overflow;
            return this;
        }

        public Builder onTime(LocalTime onTime) {
            this.onTime = onTime;
            return this;
        }

        public Builder offTime(LocalTime offTime) {
            this.offTime = offTime;
            return this;
        }

        public CurrentState build() {
            return new CurrentState(
                    alive,
                    time,
                    waterflow,
                    sensorPin,
                    offPin,
                    action,
                    programs,
                    data,
                    pixels,
                    nextPixel,
                    nol,
                    restartCountdown,
                    pixelProgram,
                    brightness,
                    stepTime,
                    on,
                    sensorDependency,
                    timeDependency,
                    overflow,
                    onTime,
                    offTime
            );
        }
    }
}