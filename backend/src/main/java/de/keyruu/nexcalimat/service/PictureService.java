package de.keyruu.nexcalimat.service;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.FilestoreClient;
import de.keyruu.nexcalimat.filestore.exception.WrongFileTypeException;
import de.keyruu.nexcalimat.model.HasPicture;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class PictureService
{
	private static final Set<String> VALID_FILE_TYPES = Set.of("jpg", "jpeg", "png");

	@Inject
	FilestoreClient _filestoreClient;

	<T extends HasPicture> T updatePicture(T entity, FileFormData formData, PanacheRepository<T> repo, String type) throws IOException
	{
		if (!VALID_FILE_TYPES.contains(FilenameUtils.getExtension(formData.filename)))
		{
			throw new WrongFileTypeException();
		}

		String objectKey = _filestoreClient.uploadFile(formData, type, entity.getId());

		String oldPicture = entity.getPicture();
		if (oldPicture != null && !oldPicture.isEmpty() && !oldPicture.trim().isEmpty())
		{
			_filestoreClient.deleteFile(FilestoreClient.buildFullObjectKey(type, oldPicture));
		}

		entity.setPicture(objectKey);
		repo.persist(entity);

		return entity;
	}

	<T extends HasPicture> void deletePicture(T entity, PanacheRepository<T> repo, String type)
	{
		String picture = entity.getPicture();
		_filestoreClient.deleteFile(FilestoreClient.buildFullObjectKey(type, picture));

		entity.setPicture(null);
		repo.persist(entity);
	}
}
