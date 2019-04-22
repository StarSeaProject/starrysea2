package top.starrysea.object.view.in;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "省份对象", description = "省份对象")
public class ProvinceForOne {

	@ApiModelProperty(value = "省份id", name = "provinceId")
	@NotNull(message = "省份id不能为空")
	private Integer provinceId;

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

}
