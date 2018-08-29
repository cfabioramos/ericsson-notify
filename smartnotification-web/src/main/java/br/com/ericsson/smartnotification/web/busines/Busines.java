package br.com.ericsson.smartnotification.web.busines;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ericsson.smartnotification.entities.AbstractDocument;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.web.domain.FilterView;
import br.com.ericsson.smartnotification.web.dto.AbstractDto;

@SuppressWarnings("rawtypes")
public abstract class Busines<T extends AbstractDto> {
    
    @SuppressWarnings("unchecked")
    public void save(T dto) throws ApplicationException {
        if(dto.getId() != null && !dto.getId().isEmpty()) {
            Optional<AbstractDocument> documentOpt = getRepository().findById(dto.getId());
            if(documentOpt.isPresent()) {
                AbstractDocument document = documentOpt.get();
                dto.setValuesFromDocument(document);
                getRepository().save(document);
            }else {
                throw new ApplicationException(String.format("Documento s% com o id s% não existe no repositório", dto.getDocumentName(), dto.getId()));
            }
        }else {
            dto.setId(null);
            try {
                AbstractDocument document = (AbstractDocument) getRepository().save(dto.getNewDocument());
                dto.setId(document.getId());
            }catch (DuplicateKeyException e) {
                throw new ApplicationException(String.format("Existe um %s já cadastrado com esse identificador", getDto().getDocumentName()));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public T getDto(String id) throws ApplicationException {
        Optional<AbstractDocument> documentOpt = getRepository().findById(id);
        if(documentOpt.isPresent()) {
            return (T) getDto().setValuesFromDto(documentOpt.get());
        }else {
            throw new ApplicationException(String.format("Documento s% com o id s% não existe no repositório", getDto().getDocumentName(), id));
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<T> list(int ini, int end) throws ApplicationException{
        Page<AbstractDocument> page =  getRepository().findAll(PageRequest.of(ini, end));
        return (List<T>) getDto().setValuesFromDtos(page.getContent(), getDto().getClass());
    } 
    
    @SuppressWarnings("unchecked")
    public void delete(T dto) throws ApplicationException {
        getRepository().delete(dto.getNewDocument());
    }
    
    @SuppressWarnings("unchecked")
	public List<T> list() throws ApplicationException{
    	List<AbstractDocument> list =  getRepository().findAll();
        return getDto().setValuesFromDtos(list, getDto().getClass());
    }
    
    public abstract List<T> list(FilterView filterView, int startPage, int maxItens) throws ApplicationException;
    
    protected abstract T getDto(); 
    
    protected abstract MongoRepository getRepository();
    
    public long total() {
    	return getRepository().count();
    }

}
