package de.keyruu.nexcalimat.service;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.keyruu.nexcalimat.filestore.FileFormData;
import de.keyruu.nexcalimat.filestore.FilestoreClient;
import de.keyruu.nexcalimat.filestore.PictureType;
import de.keyruu.nexcalimat.model.HasPicture;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class PictureService
{
  @Inject
  FilestoreClient _filestoreClient;

  <T extends HasPicture> T updatePicture(T entity, FileFormData formData, PanacheRepository<T> repo) throws IOException
  {
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
}
