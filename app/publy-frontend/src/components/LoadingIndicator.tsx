type LoadingIndicatorProps = {
  children?: React.ReactNode;
  secondary?: boolean;
};
export default function LoadingIndicator({
  children,
  secondary,
}: LoadingIndicatorProps) {
  return secondary ? (
    <div className="Spinner secondary">
      <div className="bounce bounce1" />
      <div className="bounce bounce2" />
      <div className="bounce bounce3" />
    </div>
  ) : (
    <div className="Container">
      <div className="Spinner">
        {children && <h1>{children}</h1>}
        <div className="bounce bounce1" />
        <div className="bounce bounce2" />
        <div className="bounce bounce3" />
      </div>
    </div>
  );
}
