package pink.zak.minestom.operadora.update;

import org.jetbrains.annotations.NotNull;

public record OperadoraVersion(@NotNull String commitHash, @NotNull String version, int buildNumber) {

    public String shortCommitHash() {
        return this.commitHash.length() >= 8 ? this.commitHash.substring(0, 7) : this.commitHash;
    }
}
