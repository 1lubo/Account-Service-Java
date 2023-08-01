package account.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SecurityLogService {
	@Autowired
	SecurityLogRepository logRepository;

	public void saveLog(SecurityLog log) {
		logRepository.save(log);
	}

	public ArrayList<SecurityLogDTO> findAll(){
		ArrayList<SecurityLogDTO> logDTOS = new ArrayList<>();
		for(SecurityLog log : logRepository.findAll()) {
			logDTOS.add(log.convertToDTO());
		}
		return logDTOS;
	}
}
