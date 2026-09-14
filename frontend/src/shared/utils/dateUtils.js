import dayjs from 'dayjs';

export function toApiDate(date) {
    if (!date) return null;
    return dayjs(date).format('YYYY-MM-DD');        
}

export function toApiDateTime(date) {
    if (!date) return null;
    return dayjs(date).format('YYYY-MM-DDTHH:mm:ss');
}

export function toDisplayDate(isoDateString) {
    if (!isoDateString) return '';
    return dayjs(isoDateString).format('DD/MM/YYYY');
}

export function toDisplayDateTime(isoDateTimeString) {
    if (!isoDateTimeString) return '';
    return dayjs(isoDateTimeString).format('DD/MM/YYYY HH:mm:ss');
}