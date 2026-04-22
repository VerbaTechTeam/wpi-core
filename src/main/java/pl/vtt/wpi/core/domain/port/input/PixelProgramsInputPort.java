package pl.vtt.wpi.core.domain.port.input;

import java.util.List;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class PixelProgramsInputPort implements InputPort<List<PixelProgram>> {
    private final RequestFactory<List<PixelProgram>> requestFactory;
    private final RequestSender requestSender;

    public PixelProgramsInputPort(RequestFactory<List<PixelProgram>> requestFactory,
                                  RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(List<PixelProgram> obj) throws InputPortException {
        if (obj == null) {
            throw new InputPortException("Pixel programs cannot be null");
        }
        try {
            if (obj.contains(null)) {
                throw new InputPortException("Pixel programs list cannot contain null elements");
            }
        } catch (NullPointerException _) {
            // contains() may throw NullPointerException
            // if obj is not a List implementation that supports null elements
        }
        try {
            Request<List<PixelProgram>> request = requestFactory.create(
                    obj, Method.PUT, RequestTarget.PIXEL_PROGRAMS_UPDATE
            );
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot send pixel programs", e);
        }
    }
}
