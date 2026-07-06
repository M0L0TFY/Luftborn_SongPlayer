package com.luftborn.luftborn_backend.security.evaluator;

import com.luftborn.luftborn_backend.model.Playlist;
import com.luftborn.luftborn_backend.repository.PlaylistRepository;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("playlistSecurity")
@RequiredArgsConstructor
public class PlaylistSecurity {
    private final PlaylistRepository playlistRepository;

    @Transactional(readOnly = true)
    public boolean isOwnerOfPlaylist(Long playlistId) {
        //Check if SecurityContext is populated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails))
            return false;

        return playlistRepository.findById(playlistId)
                .map(playlist -> playlist.getOwner().getId().equals(userDetails.id()))
                .orElse(false); //Playlist doesn't exist
    }

    @Transactional(readOnly = true)
    public boolean publicPlaylist(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .map(Playlist::isPublic)
                .orElse(false); //Playlist is private
    }
}
