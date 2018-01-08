package cn.itcast.bos.web.action.report;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ReportAction extends BaseAction<WayBill>{
	@Autowired
	private WayBillService wayBillService;
	@Action("report_exportXls")
	private String exportXls() throws Exception{
		// 查询出 满足当前条件 结果数据
		List<WayBill> wayBills = wayBillService.findWayBills(model);
		// 生成Excel文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("运单数据");
		//表头
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("运单号");
		headRow.createCell(1).setCellValue("寄件人");
		headRow.createCell(2).setCellValue("寄件人电话");
		headRow.createCell(3).setCellValue("寄件人地址");
		headRow.createCell(4).setCellValue("收件人");
		headRow.createCell(5).setCellValue("收件人电话");
		headRow.createCell(6).setCellValue("收件人电话");
		//表格数据
		for (WayBill wayBill : wayBills) {
			HSSFRow createRow = sheet.createRow(sheet.getLastRowNum()+1);
			createRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			createRow.createCell(1).setCellValue(wayBill.getSendName());
			createRow.createCell(2).setCellValue(wayBill.getSendMobile());
			createRow.createCell(3).setCellValue(wayBill.getSendAddress());
			createRow.createCell(4).setCellValue(wayBill.getRecName());
			createRow.createCell(5).setCellValue(wayBill.getRecMobile());
			createRow.createCell(6).setCellValue(wayBill.getRecAddress());
		}
		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		String fileName = "运单数据.xls";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		fileName = FileUtils.encodeDownloadFilename(fileName, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition","attachment;filename="+fileName);
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		hssfWorkbook.write(outputStream);
		// 关闭
		hssfWorkbook.close();
		return NONE;
	}
}
