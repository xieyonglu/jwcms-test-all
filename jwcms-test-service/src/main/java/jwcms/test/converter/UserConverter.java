package jwcms.test.converter;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import jwcms.common.converter.Converter;
import jwcms.test.model.TUser;
import jwcms.test.model.User;


/**
 * <h1>咨询申请记录themis_consult_service实体封装类属性转换器</h1>
 * 
 * @author yonglu.xie
 * @date 2017/08/28
 */
@Service
public class UserConverter extends Converter<User, TUser> {
	
	@Override
	protected TUser defaultInvert(User source) throws Exception {
		TUser target = new TUser();
		
		target.setId(source.getId());
		target.setA(source.getA());
		target.setB(source.getB());
		
		target.setIsDelete(source.getIsDelete());
		target.setCreator(source.getCreator());
		if(source.getCreatedAt() != null) {
			target.setCreatedAt(new Timestamp(source.getCreatedAt()));
		}
		target.setModifier(source.getModifier());
		if(source.getUpdatedAt() != null) {
			target.setUpdatedAt(new Timestamp(source.getUpdatedAt()));
		}
		
		return target;
	}
	
	@Override
	protected User defaultConvert(TUser source) throws Exception {
		User target = new User();
		
		target.setId(source.getId());
		target.setId(source.getId());
		target.setA(source.getA());
		target.setB(source.getB());
		
		
		target.setIsDelete(source.getIsDelete());
		target.setCreator(source.getCreator());
		if(source.getCreatedAt() != null) {
			target.setCreatedAt(source.getCreatedAt().getTime());
		}
		target.setModifier(source.getModifier());
		if(source.getUpdatedAt() != null) {
			target.setUpdatedAt(source.getUpdatedAt().getTime());
		}
		
		return target;
	}
	
}
