package de.keyruu.nexcalimat.service;

import java.io.IOException;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.FilestoreClient;
import de.keyruu.nexcalimat.filestore.PictureType;
import de.keyruu.nexcalimat.filestore.exception.WrongFileTypeException;
import de.keyruu.nexcalimat.model.HasPicture;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class PictureService
{
  private static final Set<String> VALID_FILE_TYPES = Set.of("jpg", "jpeg", "png");

  @Inject
  FilestoreClient _filestoreClient;

  <T extends HasPicture> T updatePicture(T entity, FileFormData formData, PanacheRepository<T> repo) throws IOException
  {
    if (VALID_FILE_TYPES.contains(FilenameUtils.getExtension(formData.filename)) == false)
    {
      throw new WrongFileTypeException();
    }

    String objectKey = _filestoreClient.uploadFile(formData, PictureType.ACCOUNT, entity.getId());

    String oldPicture = entity.getPicture();
    if (oldPicture != null && oldPicture.isEmpty() == false && oldPicture.trim().isEmpty() == false)
    {
      _filestoreClient.deleteFile(FilestoreClient.buildFullObjectKey(PictureType.ACCOUNT, oldPicture));
    }

    entity.setPicture(objectKey);
    repo.persist(entity);

    return entity;
  }

  <T extends HasPicture> void deletePicture(T entity, PanacheRepository<T> repo)
  {
    String picture = entity.getPicture();
    _filestoreClient.deleteFile(FilestoreClient.buildFullObjectKey(PictureType.ACCOUNT, picture));

    entity.setPicture(null);
    repo.persist(entity);
  }
}
