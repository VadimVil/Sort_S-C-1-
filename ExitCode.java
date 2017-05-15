
public enum ExitCode {
    SUCCESS(0),
    FAILURE(1);

    private int exitCode;

    ExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public int getCode() {
        return exitCode;
    }
}
