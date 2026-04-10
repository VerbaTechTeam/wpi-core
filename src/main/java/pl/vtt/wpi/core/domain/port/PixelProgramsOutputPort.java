package pl.vtt.wpi.core.domain.port;

import java.util.List;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class PixelProgramsOutputPort implements OutputPort<List<PixelProgram>> {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, List<PixelProgram>> requestHandler;

    public PixelProgramsOutputPort(RequestFactory<Void> requestFactory,
                                   RequestHandler<Void, List<PixelProgram>> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public List<PixelProgram> load() throws OutputPortException {
        try {
            return requestHandler.handle(requestFactory.create(Method.GET, RequestTarget.PIXEL_PROGRAMS_READ, null));
        } catch (Exception e) {
            throw new OutputPortException("Cannot load pixel programs", e);
        }
    }
}
