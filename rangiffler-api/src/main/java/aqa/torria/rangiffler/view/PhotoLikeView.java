package aqa.torria.rangiffler.view;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface PhotoLikeView {
    UUID getUserId();
    String getUsername();
    OffsetDateTime getCreatedDate();
}
