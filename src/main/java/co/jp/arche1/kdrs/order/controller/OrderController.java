package co.jp.arche1.kdrs.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.order.dto.OrderDto;
import co.jp.arche1.kdrs.order.dto.OrderSearchManyDto;
import co.jp.arche1.kdrs.order.service.OrderService;

@RestController
public class OrderController {

	final static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	OrderService orderService;

	@Autowired
	MessageSource msg;

	// 変更作業登録・更新・削除
	@RequestMapping(value = "/Order/RegisterUpdateDeleteOrder", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public OrderDto registerUpdateDeleteOrder(@RequestBody OrderDto orderDto) throws Exception  {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		orderService.createUpdateDelete(orderDto);

		return orderDto;
	}

	// 変更作業概要検索
		@RequestMapping(value = "/Order/ReferOrderMany", method = RequestMethod.GET)
		@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
		public OrderSearchManyDto referOrderMany(
				@RequestParam(name = "userId", required = false) Integer userId,
				@RequestParam(name = "orderNo", required = false) Integer orderNo,
				@RequestParam(name = "privConstId", required = false) Integer privConstId,
				@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
			logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

			OrderSearchManyDto orderSearchManyDto = new OrderSearchManyDto();
			OrderSearchManyDto.RequestHd regHd = orderSearchManyDto.getReqHd();
			regHd.setUserId(userId);
			regHd.setOrderNo(orderNo);
			regHd.setPrivConstId(privConstId);
			if (deleted != null) {
				if (deleted.equalsIgnoreCase("FALSE")) {
					regHd.setDeleted((byte) 0);
				} else if (deleted.equalsIgnoreCase("TRUE")) {
					regHd.setDeleted((byte) 1);
				}
			} else {
				regHd.setDeleted((byte) 2);
			}
			orderService.searchMany(orderSearchManyDto);
			return orderSearchManyDto;
		}



}