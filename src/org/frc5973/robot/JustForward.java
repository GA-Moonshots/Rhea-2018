package org.frc5973.robot;
import org.strongback.command.*;

public class JustForward extends CommandGroup {
    public JustForward() {
        sequentially(new CommandA(),
                     new CommandB(),
                     new CommandC());
    }
}