package mahara.web.response.json;

import lombok.Data;

@Data
public class ErrorInfo {
	private int errCode;
	private String errMsg;
}