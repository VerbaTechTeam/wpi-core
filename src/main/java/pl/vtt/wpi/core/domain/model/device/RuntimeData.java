package pl.vtt.wpi.core.domain.model.device;

import java.time.LocalTime;
import java.util.TimeZone;

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
        TimeZone  timeZone
) {
}
