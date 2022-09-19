package com.pchr.dto;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.pchr.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

	private Long commentId;
	@Size(max = 10000,message = "댓글 내용은 10,000자 이내로 입력해주세요.")
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private String commentContent;

	private Date registeredDate;

	private Date modifiedDate;

	private BoardDTO boardDto;

	private EmployeeDTO employeeDto;
	
	public Comment toComment(CommentDTO commentDto) {
		Comment comment = Comment.builder()	
						.commentId(commentDto.getCommentId())
						.commentContent(commentDto.getCommentContent())
						.registeredDate(commentDto.getRegisteredDate())
						.modifiedDate(commentDto.getModifiedDate())
						.board(commentDto.getBoardDto().toFKBoard(commentDto.getBoardDto()))
						.emp(commentDto.getEmployeeDto().toFKManager(commentDto.getEmployeeDto()))
						.build();
		return comment;
	}
}
