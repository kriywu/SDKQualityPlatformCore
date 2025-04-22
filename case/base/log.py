import logging
from colorlog import ColoredFormatter

logger = logging.getLogger("Logger")
logger.setLevel(logging.DEBUG)  # 设置日志级别

console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)

formatter = ColoredFormatter(
    datefmt=None,
    reset=True,
    log_colors={
        'DEBUG': 'cyan',
        'INFO': 'green',
        'WARNING': 'yellow',
        'ERROR': 'red',
        'CRITICAL': 'red,bg_white',
    },
    secondary_log_colors={
        'message': {
            'DEBUG': 'cyan',
            'INFO': 'green',
            'WARNING': 'yellow',
            'ERROR': 'red',
            'CRITICAL': 'red'
        }
    },
    style='%'
)

console_handler.setFormatter(formatter)

logger.addHandler(console_handler)
