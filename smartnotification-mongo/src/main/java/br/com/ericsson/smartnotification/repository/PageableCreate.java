package br.com.ericsson.smartnotification.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.ericsson.smartnotification.constants.Constants;

public interface PageableCreate {
    
	default Pageable createPageRequest(int page) {
		return PageRequest.of(page, Constants.MAX_PAGINATION);
	}

	default Pageable createPageRequest(int page, int max) {
        return PageRequest.of(page, max);
    }

}
