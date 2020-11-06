package com.salesmanager.shop.model.customer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.shop.model.customer.attribute.PersistableCustomerAttribute;
import com.salesmanager.shop.model.security.PersistableGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;


@ApiModel(value="Customer", description="Customer model object")
public class PersistableCustomer extends CustomerEntity {

	/**
	 * 
	 */
    @ApiModelProperty(notes = "Customer password")
	private String password = null;
	private static final long serialVersionUID = 1L;
	private List<PersistableCustomerAttribute> attributes;
	private List<PersistableGroup> groups;
	private String imageUrl;

	@JsonIgnore
	private MultipartFile profilePhoto;

	public MultipartFile getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(MultipartFile profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public void setAttributes(List<PersistableCustomerAttribute> attributes) {
		this.attributes = attributes;
	}
	public List<PersistableCustomerAttribute> getAttributes() {
		return attributes;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<PersistableGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<PersistableGroup> groups) {
		this.groups = groups;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
