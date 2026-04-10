package pl.vtt.wpi.core.domain.model.device;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;

public record RuntimeData(
        Integer   nol,
        Integer   pixelProgram,
        Double    stepTime,
        Integer   brightness,
        Integer   defaultRestartCountdown,
        Boolean   overflow,
        Boolean   on,
        Boolean   sensorDependency,
        Boolean   timeDependency,
        LocalTime onTime,
        LocalTime offTime,
        ZoneId zoneId
) {
    public static final class Builder {
        private Integer   nol;
        private Integer   pixelProgram;
        private Double    stepTime;
        private Integer   brightness;
        private Integer   defaultRestartCountdown;
        private Boolean   overflow;
        private Boolean   on;
        private Boolean   sensorDependency;
        private Boolean   timeDependency;
        private LocalTime onTime;
        private LocalTime offTime;
        private ZoneId zoneId;

        public Builder() {
            this.nol = 3;
            this.pixelProgram = 0;
            this.stepTime = 1.0;
            this.brightness = 255;
            this.defaultRestartCountdown = 10;
            this.overflow = false;
            this.on = true;
            this.sensorDependency = true;
            this.timeDependency = true;
            this.onTime = LocalTime.of(8, 0);
            this.offTime = LocalTime.of(20, 0);
            this.zoneId = ZoneId.systemDefault();
        }

        public Builder from(RuntimeData data) {
            Objects.requireNonNull(data, "data cannot be null");
            if (data.nol() != null) {
                this.nol = data.nol();
            }
            if (data.pixelProgram() != null) {
                this.pixelProgram = data.pixelProgram();
            }
            if (data.stepTime() != null) {
                this.stepTime = data.stepTime();
            }
            if (data.brightness() != null) {
                this.brightness = data.brightness();
            }
            if (data.defaultRestartCountdown() != null) {
                this.defaultRestartCountdown = data.defaultRestartCountdown();
            }
            if (data.overflow() != null) {
                this.overflow = data.overflow();
            }
            if (data.on() != null) {
                this.on = data.on();
            }
            if (data.sensorDependency() != null) {
                this.sensorDependency = data.sensorDependency();
            }
            if (data.timeDependency() != null) {
                this.timeDependency = data.timeDependency();
            }
            if (data.onTime() != null) {
                this.onTime = data.onTime();
            }
            if (data.offTime() != null) {
                this.offTime = data.offTime();
            }
            if (data.zoneId() != null) {
                this.zoneId = data.zoneId();
            }
            return this;
        }

        public Builder nol(Integer nol) {
            this.nol = nol;
            return this;
        }

        public Builder pixelProgram(Integer pixelProgram) {
            this.pixelProgram = pixelProgram;
            return this;
        }

        public Builder stepTime(Double stepTime) {
            this.stepTime = stepTime;
            return this;
        }

        public Builder brightness(Integer brightness) {
            this.brightness = brightness;
            return this;
        }

        public Builder defaultRestartCountdown(Integer defaultRestartCountdown) {
            this.defaultRestartCountdown = defaultRestartCountdown;
            return this;
        }

        public Builder overflow(Boolean overflow) {
            this.overflow = overflow;
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

        public Builder onTime(LocalTime onTime) {
            this.onTime = onTime;
            return this;
        }

        public Builder offTime(LocalTime offTime) {
            this.offTime = offTime;
            return this;
        }

        public Builder zoneId(ZoneId zoneId) {
            this.zoneId = zoneId;
            return this;
        }

        public RuntimeData build() {
            return new RuntimeData(
                    nol,
                    pixelProgram,
                    stepTime,
                    brightness,
                    defaultRestartCountdown,
                    overflow,
                    on,
                    sensorDependency,
                    timeDependency,
                    onTime,
                    offTime,
                    zoneId
            );
        }
    }
}
