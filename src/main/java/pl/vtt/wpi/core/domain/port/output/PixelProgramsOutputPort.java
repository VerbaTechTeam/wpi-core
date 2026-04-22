package pl.vtt.wpi.core.domain.port.output;

import java.util.List;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.OutputPort;

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
            return requestHandler.handle(requestFactory.create(null, Method.GET, RequestTarget.PIXEL_PROGRAMS_READ));
        } catch (Exception e) {
            throw new OutputPortException("Cannot load pixel programs", e);
        }
    }
}
